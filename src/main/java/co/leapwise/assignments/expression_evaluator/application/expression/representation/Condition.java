package co.leapwise.assignments.expression_evaluator.application.expression.representation;

import lombok.Builder;

@Builder
public record Condition(String field, Operator operator, Object value) {
}