package co.leapwise.assignments.expression_evaluator.application.expression;

import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import lombok.NoArgsConstructor;

import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.INVALID_EXPRESSION;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ComparisonUtils {

    private static final double DOUBLE_TOLERANCE = 1e-9;

    @SuppressWarnings("unchecked")
    public static int compare(Object fieldValue, Object conditionValue) {
        if (isNull(fieldValue) && nonNull(conditionValue)) {
            return 1;
        } else if (isNull(conditionValue) && nonNull(fieldValue)) {
            return -1;
        } else if (isNull(fieldValue)) {
            return 0;
        }

        return switch (fieldValue) {
            case String s when conditionValue instanceof String -> compareStrings(s, (String) conditionValue);
            case Double v when conditionValue instanceof Double -> compareDoubles(v, (Double) conditionValue);
            case Comparable comparable when conditionValue instanceof Comparable ->
                    ((Comparable<Object>) fieldValue).compareTo(conditionValue);
            default -> throw new ApplicationError(INVALID_EXPRESSION,
                                                  "Values are not comparable: " + fieldValue + ", " + conditionValue);
        };

    }

    private static int compareStrings(String fieldValue, String conditionValue) {
        return fieldValue.compareToIgnoreCase(conditionValue);
    }

    private static int compareDoubles(Double fieldValue, Double conditionValue) {
        double difference = fieldValue - conditionValue;
        if (Math.abs(difference) < DOUBLE_TOLERANCE) {
            return 0;
        }
        return difference > 0 ? 1 : -1;
    }
}