package co.leapwise.assignments.expression_evaluator.application.service;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.EvaluateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.expression.ExpressionEvaluator;
import co.leapwise.assignments.expression_evaluator.application.expression.ExpressionValidator;
import co.leapwise.assignments.expression_evaluator.application.mapper.JsonFlattener;
import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import co.leapwise.assignments.expression_evaluator.domain.model.Expression;
import co.leapwise.assignments.expression_evaluator.domain.service.ExpressionDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.EXPRESSION_NOT_FOUND;
import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.INVALID_EXPRESSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ExpressionApplicationServiceTest {

    private final String expressionId = UUID.randomUUID().toString();
    private final Map<String, Object> jsonBody = Map.of("customer", Map.of("firstName", "John", "salary", 120));
    @Mock
    private ExpressionDomainService expressionDomainService;
    @Mock
    private ExpressionValidator expressionValidator;
    @Mock
    private ExpressionEvaluator expressionEvaluator;
    @Mock
    private JsonFlattener jsonFlattener;
    @InjectMocks
    private ExpressionApplicationService expressionApplicationService;

    @Test
    void createExpression_ValidExpression_ShouldReturnResponse() {
        CreateExpressionDTO request = CreateExpressionDTO.builder()
                                                         .name("Valid Expression")
                                                         .expression("(customer.firstName == 'John')")
                                                         .build();
        CreateExpressionResponseDTO expectedResponse = CreateExpressionResponseDTO.builder().expressionId(UUID.randomUUID()).build();

        given(expressionValidator.validateExpression(request.expression())).willReturn(true);
        given(expressionDomainService.createExpression(request.name(), request.expression())).willReturn(expectedResponse.expressionId());

        CreateExpressionResponseDTO actualResponse = expressionApplicationService.createExpression(request);

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
        then(expressionValidator).should(times(1)).validateExpression(request.expression());
        then(expressionDomainService).should(times(1)).createExpression(request.name(), request.expression());
        then(expressionValidator).shouldHaveNoMoreInteractions();
        then(expressionDomainService).shouldHaveNoMoreInteractions();
    }

    @Test
    void createExpression_InvalidExpression_ShouldThrowApplicationError() {
        CreateExpressionDTO request = CreateExpressionDTO.builder()
                                                         .name("Invalid Expression")
                                                         .expression(
                                                                 "(customer.firstName = 'John' & customer.salary < " +
                                                                         "100)")
                                                         .build();

        given(expressionValidator.validateExpression(request.expression())).willReturn(false);

        assertThatThrownBy(() -> expressionApplicationService.createExpression(request))
                .isInstanceOf(ApplicationError.class)
                .hasMessageStartingWith(INVALID_EXPRESSION);

        then(expressionValidator).should(times(1)).validateExpression(request.expression());
        then(expressionDomainService).shouldHaveNoInteractions();
        then(expressionValidator).shouldHaveNoMoreInteractions();
    }

    @Test
    void evaluate_ExpressionFound_ShouldReturnEvaluationResult() {
        Expression expression = mock(Expression.class);
        given(expressionDomainService.findById(UUID.fromString(expressionId))).willReturn(Optional.of(expression));
        given(expressionEvaluator.evaluate(expression, jsonBody)).willReturn(true);
        given(jsonFlattener.flatten(jsonBody)).willReturn(jsonBody);

        boolean result = expressionApplicationService.evaluate(
                EvaluateExpressionDTO.builder().jsonBody(jsonBody).expressionId(expressionId).build());

        assertThat(result).isTrue();
        then(expressionDomainService).shouldHaveNoMoreInteractions();
        then(expressionEvaluator).shouldHaveNoMoreInteractions();
        then(jsonFlattener).shouldHaveNoMoreInteractions();
    }

    @Test
    void evaluate_ExpressionNotFound_ShouldThrowApplicationError() {
        given(expressionDomainService.findById(UUID.fromString(expressionId))).willReturn(Optional.empty());

        assertThatThrownBy(() -> expressionApplicationService.evaluate(
                EvaluateExpressionDTO.builder().jsonBody(jsonBody).expressionId(expressionId).build()))
                .isInstanceOf(ApplicationError.class)
                .hasFieldOrPropertyWithValue("errorCode", EXPRESSION_NOT_FOUND)
                .hasFieldOrPropertyWithValue("statusCode", 404)
                .hasFieldOrPropertyWithValue("params", List.of(expressionId));

        then(expressionDomainService).should().findById(UUID.fromString(expressionId));
        then(expressionEvaluator).shouldHaveNoInteractions();
        then(jsonFlattener).shouldHaveNoInteractions();
    }
}
