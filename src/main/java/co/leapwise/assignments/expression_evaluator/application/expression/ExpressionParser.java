package co.leapwise.assignments.expression_evaluator.application.expression;

import co.leapwise.assignments.expression_evaluator.application.expression.representation.*;
import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.INVALID_EXPRESSION;

@Component
@Slf4j
public class ExpressionParser {

    //    private static final String CONDITION_PATTERN_VALUE_LIMITS =
//            "([a-zA-Z_][a-zA-Z0-9_]*\\.?[a-zA-Z0-9_.]*)\\s*?(>=|<=|==|!=|>|<)\\s*?((\\d+[.,]?\\d*)|'.*?'|\"
//            .*?\"|true|false|null)";
    private static final String CONDITION_PATTERN =
            "([a-zA-Z_][a-zA-Z0-9_]*\\.?[a-zA-Z0-9_.]*)\\s*?(>=|<=|==|!=|>|<)\\s*?(.*)";

    @Cacheable(cacheNames = "expressions", key = "#identifier")
    public ExpressionRepresentation parse(String identifier, String expression) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsing expression with identifier: {}", identifier);
        }
        return parseInternal(expression);
    }

    protected ExpressionRepresentation parseInternal(String expression) {
        try {
            List<String> tokens = Arrays.asList(tokenize(expression));
            return buildExpressionRepresentation(tokens);
        } catch (Exception e) {
            LOG.error("Error parsing expression: {}", expression, e);
            if (e instanceof ApplicationError) {
                throw e;
            }
            throw new ApplicationError(INVALID_EXPRESSION, "invalid expression: " + expression);
        }
    }

    private String[] tokenize(String expression) {
        // - Splits logical operators and parentheses into separate tokens, preserving string literals.
        return expression.split(
                "(?<=\\()|(?=\\))|(?<=\\)|\\&\\&|\\|\\||\\|\\||\\bAND\\b|\\bOR\\b|\\bNOT\\b)|(?=\\)" +
                        "|\\&\\&|\\|\\||\\|\\||\\bAND\\b|\\bOR\\b|\\bNOT\\b)");
    }

    private ExpressionRepresentation buildExpressionRepresentation(List<String> tokens) {
        Stack<ExpressionRepresentation> stack = new Stack<>();
        Stack<LogicalOperator> operatorStack = new Stack<>();
        Stack<Integer> parenthesisStack = new Stack<>();

        for (String s : tokens) {
            String token = s.trim();
            switch (token.toUpperCase()) {
                case "(":
                    parenthesisStack.push(stack.size());
                    break;
                case ")":
                    int startIndex = parenthesisStack.pop();
                    List<ExpressionRepresentation> subExpression = new ArrayList<>();
                    while (stack.size() > startIndex) {
                        subExpression.addFirst(stack.pop());
                    }
                    stack.push(buildSubExpression(subExpression, operatorStack));
                    break;
                case "&&":
                case "AND":
                    operatorStack.push(LogicalOperator.AND);
                    break;
                case "||":
                case "OR":
                    operatorStack.push(LogicalOperator.OR);
                    break;
                case "!":
                case "NOT":
                    operatorStack.push(LogicalOperator.NOT);
                    break;
                case "": //TODO we get this because of the split regex is not the best
                    break;
                default:
                    Condition condition = parseCondition(token);
                    stack.push(new ExpressionRepresentation(condition));
            }
        }

        return buildSubExpression(stack, operatorStack);
    }

    private ExpressionRepresentation buildSubExpression(List<ExpressionRepresentation> subExpression,
                                                        Stack<LogicalOperator> operatorStack) {
        while (subExpression.size() > 1) {
            ExpressionRepresentation right = subExpression.removeLast();
            LogicalOperator operator = operatorStack.pop();
            ExpressionRepresentation left = subExpression.removeLast();
            subExpression.add(new ExpressionRepresentation(operator, left, right));
        }
        return subExpression.getFirst();
    }

    private Condition parseCondition(String condition) {
        Pattern pattern = Pattern.compile(CONDITION_PATTERN);
        Matcher matcher = pattern.matcher(condition);
        if (matcher.matches()) {
            String field = matcher.group(1).trim();
            String operatorSymbol = matcher.group(2).trim();
            String value = matcher.group(3).trim();
            return new Condition(field, Operator.fromString(operatorSymbol), ValueParser.parseValueToItsObject(value));
        }
        throw new ApplicationError(INVALID_EXPRESSION, "invalid condition: " + condition);
    }
}
