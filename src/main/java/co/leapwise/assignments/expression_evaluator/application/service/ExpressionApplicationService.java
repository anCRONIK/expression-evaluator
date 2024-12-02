package co.leapwise.assignments.expression_evaluator.application.service;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.domain.service.ExpressionDomainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class ExpressionApplicationService {

    private final ExpressionDomainService expressionDomainService;

    public CreateExpressionResponseDTO createExpression(@Valid CreateExpressionDTO createExpression) {
        return expressionDomainService.createExpression(createExpression.name(), createExpression.expression());
    }

//    private final ExpressionRepository expressionRepository;
//    private final ExpressionParser expressionParser;
//
//    public UUID createExpression(@Valid CreateExpressionDTO expressionRequest) {
//        // Parse the expression (this can be a custom parser for logical expressions)
//        List<ExpressionComponent> parsedExpression = expressionParser.parse(expressionRequest.getValue());
//
//        // Create and save the LogicalExpression entity
//        LogicalExpression logicalExpression = new LogicalExpression();
//        logicalExpression.setName(expressionRequest.getName());
//        logicalExpression.setExpression(expressionRequest.getValue());
//        logicalExpression.setParsedExpression(parsedExpression);
//
//        LogicalExpression savedExpression = expressionRepository.save(logicalExpression);
//
//        return savedExpression.getId();
//    }
}
