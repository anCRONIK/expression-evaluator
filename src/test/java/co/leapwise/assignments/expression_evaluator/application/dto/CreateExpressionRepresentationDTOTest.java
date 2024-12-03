package co.leapwise.assignments.expression_evaluator.application.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateExpressionRepresentationDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void createExpressionDTO_ValidData_ShouldNotHaveViolations() {
        CreateExpressionDTO validDTO = CreateExpressionDTO.builder()
                                                          .name("Valid Name")
                                                          .expression("Valid Expression")
                                                          .build();

        Set<ConstraintViolation<CreateExpressionDTO>> violations = validator.validate(validDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void createExpressionDTO_NameIsNull_ShouldHaveViolation() {
        CreateExpressionDTO invalidDTO = CreateExpressionDTO.builder()
                                                            .name(null)
                                                            .expression("Valid Expression")
                                                            .build();

        Set<ConstraintViolation<CreateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isIn("must not be null");
    }

    @Test
    void createExpressionDTO_NameIsBlank_ShouldHaveViolation() {
        CreateExpressionDTO invalidDTO = CreateExpressionDTO.builder()
                                                            .name("")
                                                            .expression("Valid Expression")
                                                            .build();

        Set<ConstraintViolation<CreateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isIn("size must be between 2 and 150");
    }

    @Test
    void createExpressionDTO_NameTooLong_ShouldHaveViolation() {
        String longName = "A".repeat(151);
        CreateExpressionDTO invalidDTO = CreateExpressionDTO.builder()
                                                            .name(longName)
                                                            .expression("Valid Expression")
                                                            .build();

        Set<ConstraintViolation<CreateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("size must be between 2 and 150");
    }

    @Test
    void createExpressionDTO_ExpressionIsNull_ShouldHaveViolation() {
        CreateExpressionDTO invalidDTO = CreateExpressionDTO.builder()
                                                            .name("Valid Name")
                                                            .expression(null)
                                                            .build();

        Set<ConstraintViolation<CreateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(2);
        assertThat(violations.iterator().next().getMessage()).isIn("must not be null", "must not be blank");
        assertThat(violations.iterator().next().getMessage()).isIn("must not be null", "must not be blank");
    }

    @Test
    void createExpressionDTO_ExpressionIsBlank_ShouldHaveViolation() {
        CreateExpressionDTO invalidDTO = CreateExpressionDTO.builder()
                                                            .name("Valid Name")
                                                            .expression("")
                                                            .build();

        Set<ConstraintViolation<CreateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be blank");
    }

    @Test
    void createExpressionDTO_ExpressionTooLong_ShouldHaveViolation() {
        String longExpression = "A".repeat(2001);  // 2001 characters, 1 character over the limit

        CreateExpressionDTO invalidDTO = CreateExpressionDTO.builder()
                                                            .name("Valid Name")
                                                            .expression(longExpression)
                                                            .build();

        Set<ConstraintViolation<CreateExpressionDTO>> violations = validator.validate(invalidDTO);

        assertThat(violations).hasSize(1);

        assertThat(violations.iterator().next().getMessage()).isEqualTo("size must be between 0 and 2000");
    }
}
