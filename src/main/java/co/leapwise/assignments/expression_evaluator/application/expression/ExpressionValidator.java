package co.leapwise.assignments.expression_evaluator.application.expression;

import org.springframework.stereotype.Component;

@Component
public class ExpressionValidator extends ExpressionParser {

    public boolean validateExpression(String expression) {
        try {
            parseInternal(expression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
