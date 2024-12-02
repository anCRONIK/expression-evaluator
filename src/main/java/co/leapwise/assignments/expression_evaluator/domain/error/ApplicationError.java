package co.leapwise.assignments.expression_evaluator.domain.error;

import lombok.Getter;

@Getter
public class ApplicationError extends RuntimeException {
    private final String errorCode;

    public ApplicationError(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
