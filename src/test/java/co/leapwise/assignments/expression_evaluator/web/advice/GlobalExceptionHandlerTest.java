package co.leapwise.assignments.expression_evaluator.web.advice;

import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import co.leapwise.assignments.expression_evaluator.web.dto.ErrorResponse;
import co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes;
import co.leapwise.assignments.expression_evaluator.web.error.ErrorMessages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private ErrorMessages errorMessages;

    @Test
    void handleApplicationException_ValidErrorCode_ShouldReturnBadRequestWithDescription() {
        String errorCode = "APPLICATION_ERROR";
        String expectedDescription = "This is a custom application error";
        ApplicationError appError = new ApplicationError(errorCode);
        given(errorMessages.getMessageDescription(errorCode)).willReturn(expectedDescription);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleApplicationException(appError);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo(errorCode);
        assertThat(response.getBody().description()).isEqualTo(expectedDescription);
    }

    @Test
    void handleApplicationException_InvalidErrorCode_ShouldReturnBadRequestWithDefaultMessage() {
        String errorCode = "UNKNOWN_ERROR";
        String expectedDescription = "No description for code " + errorCode;
        ApplicationError appError = new ApplicationError(errorCode);
        given(errorMessages.getMessageDescription(errorCode)).willReturn(expectedDescription);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleApplicationException(appError);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo(errorCode);
        assertThat(response.getBody().description()).isEqualTo(expectedDescription);
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerErrorWithDefaultErrorMessage() {
        Exception genericException = new Exception("Generic Exception");
        String expectedDescription = "No description for code " + ErrorCodes.UNKNOWN_ERROR;
        given(errorMessages.getMessageDescription(ErrorCodes.UNKNOWN_ERROR)).willReturn(expectedDescription);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(genericException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo(ErrorCodes.UNKNOWN_ERROR);
        assertThat(response.getBody().description()).isEqualTo(expectedDescription);
    }

    @Test
    void handleConstraintViolationException_ShouldReturnBadRequest() {
        String violationMessage = "Expression must not be blank";
        ConstraintViolation<?> violation = new MockConstraintViolation<>(violationMessage);
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException exception = new ConstraintViolationException(violations);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isEqualTo("INVALID_REQUEST");
        assertThat(response.getBody().description()).contains(violationMessage);
    }

    // Custom mock for ConstraintViolation to simulate violation messages
    static class MockConstraintViolation<T> implements ConstraintViolation<T> {

        private final String message;

        public MockConstraintViolation(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return "";
        }

        @Override
        public T getRootBean() {
            return null;
        }

        @Override
        public Class<T> getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> aClass) {
            return null;
        }

    }

}
