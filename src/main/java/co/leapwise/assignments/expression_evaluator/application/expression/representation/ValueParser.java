package co.leapwise.assignments.expression_evaluator.application.expression.representation;

import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static co.leapwise.assignments.expression_evaluator.domain.error.ErrorCodes.INVALID_EXPRESSION;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ValueParser {

    public static Object parseValueToItsObject(String value) {
        if (value.startsWith("'") || value.startsWith("\"")) {
            return parseString(value);
        } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return parseBoolean(value);
        } else if (value.equalsIgnoreCase("null")) {
            return null;
        } else if (value.contains("/") || value.contains("-")) {
            return parseDate(value);
        } else {
            return parseNumber(value);
        }
    }

    private static String parseString(String value) {
        if (!value.endsWith("'") && !value.endsWith("\"")) {
            throw new ApplicationError(INVALID_EXPRESSION, "invalid string value: " + value);
        }
        return value.substring(1, value.length() - 1);
    }

    private static Boolean parseBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    private static LocalDate parseDate(String value) {
        DateTimeFormatter formatter =
                value.contains("/") ? DateTimeFormatter.ofPattern("yyyy/MM/dd") : DateTimeFormatter.ISO_DATE;
        return LocalDate.parse(value, formatter);
    }

    private static Number parseNumber(String value) {
        if (value.contains(".")  || value.contains(",")) {
            return Double.parseDouble(value); //TODO support different formats depending on locale?
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return Long.parseLong(value);
        }
    }
}