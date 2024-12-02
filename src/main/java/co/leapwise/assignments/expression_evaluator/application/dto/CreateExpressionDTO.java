package co.leapwise.assignments.expression_evaluator.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateExpressionDTO(@NotNull @Size(min = 2, max = 150) String name, @NotNull @NotBlank @Size(max = 2000) String expression) {
}
