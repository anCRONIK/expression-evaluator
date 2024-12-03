package co.leapwise.assignments.expression_evaluator.web.advice;

import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import co.leapwise.assignments.expression_evaluator.web.dto.ErrorResponse;
import co.leapwise.assignments.expression_evaluator.web.error.ErrorMessages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.*;

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
        String params = ex.getParams().isEmpty() ? "" : " " + Arrays.toString(ex.getParams().toArray());
        return new ResponseEntity<>(new ErrorResponse(ex.getErrorCode(), description + params, UUID.randomUUID().toString()),
                                    HttpStatus.resolve(ex.getStatusCode()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String description = errorMessages.getMessageDescription(DATABASE_ERROR);
        return new ResponseEntity<>(
                new ErrorResponse(DATABASE_ERROR, description, UUID.randomUUID().toString()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder description = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            description.append(violation.getMessage()).append(" ");
        }

        return new ResponseEntity<>(
                new ErrorResponse(INVALID_REQUEST, description.toString().trim(), UUID.randomUUID().toString()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        UUID errorId = UUID.randomUUID();
        String description = errorMessages.getMessageDescription(UNKNOWN_ERROR);
        return new ResponseEntity<>(new ErrorResponse(UNKNOWN_ERROR, description, errorId.toString()),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}