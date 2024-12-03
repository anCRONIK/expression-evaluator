package co.leapwise.assignments.expression_evaluator.application.expression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ExpressionValidatorTest {

    @InjectMocks
    private ExpressionValidator expressionValidator;

    @Test
    void validateExpression_ValidExpression_ShouldReturnTrue() {
        String expression = "(customer.firstName == 'JOHN' && customer.salary < 100)";
        boolean isValid = expressionValidator.validateExpression(expression);

        assertThat(isValid).isTrue();
    }

    @Test
    void validateExpression_InvalidExpression_ShouldReturnFalse() {
        String expression = "customer.firstName = 'JOHN' & customer.salary < 100)";
        boolean isValid = expressionValidator.validateExpression(expression);

        assertThat(isValid).isFalse();
    }
}
