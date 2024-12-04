package co.leapwise.assignments.expression_evaluator.application.expression;

import co.leapwise.assignments.expression_evaluator.application.expression.representation.Condition;
import co.leapwise.assignments.expression_evaluator.application.expression.representation.ExpressionRepresentation;
import co.leapwise.assignments.expression_evaluator.domain.model.Expression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static co.leapwise.assignments.expression_evaluator.application.expression.ComparisonUtils.compare;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class ExpressionEvaluator {

    private final ExpressionParser expressionParser;

    public boolean evaluate(Expression expression, Map<String, Object> jsonBody) {
        ExpressionRepresentation expressionRepresentation =
                expressionParser.parse(expression.getIdentifier().toString(), expression.getExpression());
        return evaluate(expressionRepresentation, jsonBody);
    }

    private boolean evaluate(ExpressionRepresentation expressionRepresentation, Map<String, Object> jsonBody) {
        if (nonNull(expressionRepresentation.condition())) {
            return evaluateCondition(expressionRepresentation.condition(), jsonBody);
        }

        boolean leftResult = evaluate(expressionRepresentation.left(), jsonBody);
        boolean rightResult = evaluate(expressionRepresentation.right(), jsonBody);

        return switch (expressionRepresentation.operator()) {
            case AND -> leftResult && rightResult;
            case OR -> leftResult || rightResult;
            case NOT -> !leftResult;
        };
    }

    private boolean evaluateCondition(Condition condition, Map<String, Object> jsonBody) {
        Object fieldValue = jsonBody.get(condition.field());
        Object conditionValue = condition.value();

        return switch (condition.operator()) {
            case EQUALS -> compare(fieldValue, conditionValue) == 0;
            case NOT_EQUALS -> isNull(conditionValue) ? checkIfFieldExists(jsonBody, condition.field()) : compare(fieldValue, conditionValue) != 0;
            case GREATER_THAN -> compare(fieldValue, conditionValue) > 0;
            case LESS_THAN -> compare(fieldValue, conditionValue) < 0;
            case GREATER_THAN_OR_EQUALS -> compare(fieldValue, conditionValue) >= 0;
            case LESS_THAN_OR_EQUALS -> compare(fieldValue, conditionValue) <= 0;
        };
    }

    private boolean checkIfFieldExists(Map<String, Object> jsonBody, String field) {
        return jsonBody.keySet().stream().anyMatch(key -> key.equals(field) || key.contains(field+".") || key.contains(field+"["));
    }

}
