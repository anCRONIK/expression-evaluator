package co.leapwise.assignments.expression_evaluator.application.dto;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluateExpressionDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void evaluateExpressionDTO_ValidData_ShouldNotHaveViolations() {
        EvaluateExpressionDTO validDTO = EvaluateExpressionDTO.builder()
                                                              .expressionId("123e4567-e89b-12d3-a456-426614174000")
                                                              .jsonBody(Map.of("key", "value"))
                                                              .build();

        Set<ConstraintViolation<EvaluateExpressionDTO>> violations = validator.validate(validDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void evaluateExpressionDTO_ExpressionIdIsNull_ShouldHaveViolation() {
        EvaluateExpressionDTO invalidDTO = EvaluateExpressionDTO.builder()
                                                                .expressionId(null)
                                                                .jsonBody(Map.of("key", "value"))
                                                                .build();

        Set<ConstraintViolation<EvaluateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(2);
        assertThat(violations.iterator().next().getMessage()).isIn("must not be null", "must not be blank");
        assertThat(violations.iterator().next().getMessage()).isIn("must not be null", "must not be blank");
    }

    @Test
    void evaluateExpressionDTO_ExpressionIdIsNotUUID_ShouldHaveViolation() {
        EvaluateExpressionDTO invalidDTO = EvaluateExpressionDTO.builder()
                                                                .expressionId("invalid-uuid")
                                                                .jsonBody(Map.of("key", "value"))
                                                                .build();

        Set<ConstraintViolation<EvaluateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must be a valid UUID");
    }

    @Test
    void evaluateExpressionDTO_JsonBodyIsNull_ShouldHaveViolation() {
        EvaluateExpressionDTO invalidDTO = EvaluateExpressionDTO.builder()
                                                                .expressionId("123e4567-e89b-12d3-a456-426614174000")
                                                                .jsonBody(null)
                                                                .build();

        Set<ConstraintViolation<EvaluateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }
}
