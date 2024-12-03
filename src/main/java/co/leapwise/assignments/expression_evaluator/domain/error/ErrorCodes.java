package co.leapwise.assignments.expression_evaluator.domain.error;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodes {

    public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";
    public static final String UNSUPPORTED_OPERATOR = "UNSUPPORTED_OPERATOR";
    public static final String INVALID_EXPRESSION = "INVALID_EXPRESSION";
    public static final String INVALID_ID_FORMAT = "INVALID_ID_FORMAT";
    public static final String EXPRESSION_NOT_FOUND = "EXPRESSION_NOT_FOUND";
    public static final String DATABASE_ERROR = "DATABASE_ERROR";

}
