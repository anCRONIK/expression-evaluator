package co.leapwise.assignments.expression_evaluator.web.dto;

import lombok.Builder;

@Builder
public record CreateExpressionResponse(String expressionId) {
}