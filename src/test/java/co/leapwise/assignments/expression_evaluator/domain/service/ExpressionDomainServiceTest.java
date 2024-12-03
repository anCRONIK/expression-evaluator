package co.leapwise.assignments.expression_evaluator.domain.service;


import co.leapwise.assignments.expression_evaluator.domain.model.Expression;
import co.leapwise.assignments.expression_evaluator.infrastructure.repository.ExpressionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ExpressionDomainServiceTest {

    @Mock
    private ExpressionRepository expressionRepository;

    @InjectMocks
    private ExpressionDomainService expressionDomainService;

    @Test
    void createExpression_ValidInputs_ShouldSaveAndReturnIdentifier() {
        String name = "Test Expression";
        String expressionValue = "a == b";
        UUID expectedIdentifier = UUID.randomUUID();

        Expression expression = Expression.builder()
                                          .name(name)
                                          .expression(expressionValue)
                                          .identifier(expectedIdentifier)
                                          .build();
        given(expressionRepository.save(any(Expression.class))).willReturn(expression);

        UUID identifier = expressionDomainService.createExpression(name, expressionValue);

        assertThat(identifier).isEqualTo(expectedIdentifier);
        then(expressionRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findById_ValidIdentifier_ShouldReturnExpression() {
        UUID identifier = UUID.randomUUID();
        Expression expression = Expression.builder()
                                          .identifier(identifier)
                                          .name("Test")
                                          .expression("a == b")
                                          .build();

        given(expressionRepository.findByIdentifier(identifier)).willReturn(Optional.of(expression));

        Optional<Expression> result = expressionDomainService.findById(identifier);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expression);
        then(expressionRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findById_InvalidIdentifier_ShouldReturnEmptyOptional() {
        UUID identifier = UUID.randomUUID();

        given(expressionRepository.findByIdentifier(identifier)).willReturn(Optional.empty());

        Optional<Expression> result = expressionDomainService.findById(identifier);

        assertThat(result).isEmpty();
        then(expressionRepository).shouldHaveNoMoreInteractions();
    }
}
