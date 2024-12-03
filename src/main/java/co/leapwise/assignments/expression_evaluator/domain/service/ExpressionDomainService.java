package co.leapwise.assignments.expression_evaluator.domain.service;

import co.leapwise.assignments.expression_evaluator.domain.model.Expression;
import co.leapwise.assignments.expression_evaluator.infrastructure.repository.ExpressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpressionDomainService {

    private final ExpressionRepository expressionRepository;

    public UUID createExpression(String name, String expressionValue) {
        Expression expression = expressionRepository.save(
                Expression.builder().name(name).expression(expressionValue).identifier(UUID.randomUUID()).build());
        return expression.getIdentifier();
    }

    public Optional<Expression> findById(UUID identifier) {
        return expressionRepository.findByIdentifier(identifier);
    }
}
