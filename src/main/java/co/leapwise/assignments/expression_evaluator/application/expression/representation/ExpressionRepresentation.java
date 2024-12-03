package co.leapwise.assignments.expression_evaluator.application.expression.representation;

import lombok.Builder;

@Builder
public record ExpressionRepresentation(LogicalOperator operator, ExpressionRepresentation left,
                                       ExpressionRepresentation right, Condition condition) {

    public ExpressionRepresentation(Condition condition) {
        this(null, null, null, condition);
    }


    public ExpressionRepresentation(LogicalOperator operator, ExpressionRepresentation left, ExpressionRepresentation right) {
        this(operator, left, right, null);
    }

}