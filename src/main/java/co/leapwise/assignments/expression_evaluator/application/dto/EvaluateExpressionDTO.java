package co.leapwise.assignments.expression_evaluator.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

import java.util.Map;

@Builder
public record EvaluateExpressionDTO(@NotNull @NotBlank @UUID String expressionId, @NotNull Map<String, Object> jsonBody) {
}
