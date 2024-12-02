package co.leapwise.assignments.expression_evaluator.web.advice;

import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import co.leapwise.assignments.expression_evaluator.web.dto.ErrorResponse;
import co.leapwise.assignments.expression_evaluator.web.error.ErrorMessages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.INVALID_REQUEST;
import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.UNKNOWN_ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ErrorMessages errorMessages;

    public GlobalExceptionHandler(ErrorMessages errorMessages) {
        this.errorMessages = errorMessages;
    }

    @ExceptionHandler(ApplicationError.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationError ex) {
        String description = errorMessages.getMessageDescription(ex.getErrorCode());
        return new ResponseEntity<>(new ErrorResponse(ex.getErrorCode(), description), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder description = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            description.append(violation.getMessage()).append(" ");
        }

        return new ResponseEntity<>(new ErrorResponse(INVALID_REQUEST, description.toString().trim()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        LOG.error("Unhandled generic exception", ex);
        String description = errorMessages.getMessageDescription(UNKNOWN_ERROR);
        return new ResponseEntity<>(new ErrorResponse(UNKNOWN_ERROR, description), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}