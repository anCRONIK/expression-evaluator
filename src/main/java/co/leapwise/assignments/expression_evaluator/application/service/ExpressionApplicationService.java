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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.EXPRESSION_NOT_FOUND;
import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.INVALID_EXPRESSION;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ExpressionApplicationService {

    private final ExpressionDomainService expressionDomainService;
    private final ExpressionValidator expressionValidator;
    private final ExpressionEvaluator expressionEvaluator;
    private final JsonFlattener jsonFlattener;

    public CreateExpressionResponseDTO createExpression(@Valid CreateExpressionDTO createExpression) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("New create expression request: {}", createExpression);
        }
        if (expressionValidator.validateExpression(createExpression.expression())) {
            UUID identifier =
                    expressionDomainService.createExpression(createExpression.name(), createExpression.expression());
            return new CreateExpressionResponseDTO(identifier);
        }
        throw new ApplicationError(INVALID_EXPRESSION);
    }

    public boolean evaluate(@Valid EvaluateExpressionDTO evaluateExpression) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("New evaluate expression request: {}", evaluateExpression);
        }
        Optional<Expression> expressionOptional =
                expressionDomainService.findById(UUID.fromString(evaluateExpression.expressionId()));
        if (expressionOptional.isEmpty()) {
            throw new ApplicationError(EXPRESSION_NOT_FOUND, 404, List.of(evaluateExpression.expressionId()));
        }
        return expressionEvaluator.evaluate(expressionOptional.get(), jsonFlattener.flatten(evaluateExpression.jsonBody()));
    }
}
