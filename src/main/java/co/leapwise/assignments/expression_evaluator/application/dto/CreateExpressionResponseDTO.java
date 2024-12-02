package co.leapwise.assignments.expression_evaluator.application.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateExpressionResponseDTO(UUID expressionId) {
}
