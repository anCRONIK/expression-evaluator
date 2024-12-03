package co.leapwise.assignments.expression_evaluator.domain.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
public class ApplicationError extends RuntimeException {
    private final String errorCode;
    private final int statusCode;
    private final List<Object> params;

    public ApplicationError(String errorCode) {
        this(errorCode, new Object[]{});
    }

    public ApplicationError(String errorCode, Object... params) {
        this(errorCode, HttpStatus.BAD_REQUEST.value(), Arrays.asList(params));
    }

    @Builder
    public ApplicationError(String errorCode, int statusCode, List<Object> params) {
        super(errorCode + ": " + params.stream().collect(StringBuilder::new, (sb, p) -> sb.append(p).append(", "), StringBuilder::append));
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.params = params;
    }
}
