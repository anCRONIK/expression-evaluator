package co.leapwise.assignments.expression_evaluator.application.expression.representation;

import lombok.Getter;

@Getter
public enum Operator {
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN_OR_EQUALS("<=");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public static Operator fromString(String symbol) {
        for (Operator operator : Operator.values()) {
            if (operator.getSymbol().equals(symbol)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + symbol);
    }
}