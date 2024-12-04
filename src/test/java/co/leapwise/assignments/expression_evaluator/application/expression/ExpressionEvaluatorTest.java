package co.leapwise.assignments.expression_evaluator.application.expression;

import co.leapwise.assignments.expression_evaluator.domain.model.Expression;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionEvaluatorTest {

    private static ExpressionEvaluator evaluator;

    @BeforeAll
    static void setUp() {
        ExpressionParser parser = new ExpressionParser();
        evaluator = new ExpressionEvaluator(parser);
    }

    @Test
    void evaluate_SimpleExpression_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.salary >= 100");
        Map<String, Object> jsonBody = Map.of("customer.salary", 150);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_SimpleExpression_ShouldReturnFalse() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.salary >= 100");
        Map<String, Object> jsonBody = Map.of("customer.salary", 50);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isFalse();
    }

    @Test
    void evaluate_SimpleExpression_StringEquals_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.firstName == 'John'");
        Map<String, Object> jsonBody = Map.of("customer.firstName", "John");

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_SimpleExpression_StringNotEquals_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.firstName != \"Doe\"");
        Map<String, Object> jsonBody = Map.of("customer.firstName", "John");

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_SimpleExpression_Null_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.address == null");
        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("customer.address", null);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_SimpleExpression_True_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.active == true");
        Map<String, Object> jsonBody = Map.of("customer.active", true);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_SimpleExpression_False_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.active == false");
        Map<String, Object> jsonBody = Map.of("customer.active", false);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_SimpleExpression_Null2_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.address == null");
        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("customer.address", null);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_SimpleExpression_Double_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.salary == 150.5");
        Map<String, Object> jsonBody = Map.of("customer.salary", 150.5);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_SimpleExpression_Long_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1", "customer.id == 1234567890123456789");
        Map<String, Object> jsonBody = Map.of("customer.id", 1234567890123456789L);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_DateEquals_ShouldReturnTrue() {
        String expression = "customer.joinDate == 2025-01-01";
        Map<String, Object> jsonBody = Map.of("customer.joinDate", LocalDate.of(2025, 1, 1));

        boolean result = evaluator.evaluate(new Expression(1L, UUID.randomUUID(), "1", expression), jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_DateNotEquals_ShouldReturnTrue() {
        String expression = "customer.joinDate != 2023-01-01";
        Map<String, Object> jsonBody = Map.of("customer.joinDate", LocalDate.of(2022, 12, 31));

        boolean result = evaluator.evaluate(new Expression(1L, UUID.randomUUID(), "1", expression), jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_DateGreaterThan_ShouldReturnTrue() {
        String expression = "customer.joinDate > 2023-01-01";
        Map<String, Object> jsonBody = Map.of("customer.joinDate", LocalDate.of(2023, 1, 2));

        boolean result = evaluator.evaluate(new Expression(1L, UUID.randomUUID(), "1", expression), jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_ComplexExpression_And2_ShouldReturnTrue() {
        Expression expression =
                new Expression(1L, UUID.randomUUID(), "1", "customer.salary >= 100 && customer.active == true");
        Map<String, Object> jsonBody = Map.of("customer.salary", 150, "customer.active", true);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_ComplexExpression_Or2_ShouldReturnTrue() {
        Expression expression =
                new Expression(1L, UUID.randomUUID(), "1", "customer.salary >= 100 || customer.active == false");
        Map<String, Object> jsonBody = Map.of("customer.salary", 50, "customer.active", false);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_ComplexExpression_And_ShouldReturnTrue() {
        Expression expression =
                new Expression(1L, UUID.randomUUID(), "1", "customer.salary >= 100 && customer.active == true");
        Map<String, Object> jsonBody = Map.of("customer.salary", 150, "customer.active", true);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_ComplexExpression_Or_ShouldReturnTrue() {
        Expression expression =
                new Expression(1L, UUID.randomUUID(), "1", "customer.salary >= 100 || customer.active == false");
        Map<String, Object> jsonBody = Map.of("customer.salary", 50, "customer.active", false);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_ComplexExpression_ShouldReturnTrue() {
        Expression expression =
                new Expression(1L, UUID.randomUUID(), "1",
                               "(customer.salary >= 100 && customer.firstName == \"John\") || customer.age < 30");
        Map<String, Object> jsonBody = Map.of("customer.salary", 150, "customer.firstName", "John", "customer.age", 25);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_ComplexExpression_ShouldReturnFalse() {
        Expression expression =
                new Expression(1L, UUID.randomUUID(), "1",
                               "(customer.salary >= 100 && customer.firstName == \"John\") || customer.age < 30");
        Map<String, Object> jsonBody = Map.of("customer.salary", 50, "customer.firstName", "Doe", "customer.age", 35);

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isFalse();
    }

    @Test
    void evaluate_AssignmentExpression_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1",
                                               "(customer.firstName == 'JOHN' && customer.salary < 100) || (customer" +
                                                       ".address != null && customer.address.city == 'Washington')");
        Map<String, Object> jsonBody = Map.of(
                "customer.firstName", "JOHN",
                "customer.lastName", "DOE",
                "customer.address.city", "Chicago",
                "customer.address.zipCode", 1234,
                "customer.address.street", "56th",
                "customer.address.houseNumber", 2345,
                "customer.salary", 99,
                "customer.type", "BUSINESS"
        );

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_AssignmentExpressionWithArray_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1",
                                               "(customer.firstName == 'JOHN' && customer.salary > 100) || (customer" +
                                                       ".address != null && customer.address[0].city == 'CHICAGO')");
        Map<String, Object> jsonBody = Map.of(
                "customer.firstName", "JOHN",
                "customer.lastName", "DOE",
                "customer.address[0].city", "Chicago",
                "customer.address[0].zipCode", 1234,
                "customer.address[0].street", "56th",
                "customer.address[0].houseNumber", 2345,
                "customer.salary", 99,
                "customer.type", "BUSINESS"
        );

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_NestedExpression_ShouldReturnTrue() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1",
                                               "(customer.age >= 30 AND (customer.salary > 50000 OR customer" +
                                                       ".department == \"Engineering\")) OR (customer.status == " +
                                                       "\"Active\" AND customer.joinDate <= \"2023-01-01\")");
        Map<String, Object> jsonBody =
                Map.of("customer.age", 35, "customer.salary", 60000, "customer.department", "Engineering",
                       "customer.status", "Active", "customer.joinDate", "2022-12-31");

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isTrue();
    }

    @Test
    void evaluate_NestedExpression_ShouldReturnFalse() {
        Expression expression = new Expression(1L, UUID.randomUUID(), "1",
                                               "(customer.age >= 30 AND (customer.salary > 50000 OR customer" +
                                                       ".department == \"Engineering\")) OR (customer.status == " +
                                                       "\"Active\" AND customer.joinDate <= \"2023-01-01\")");
        Map<String, Object> jsonBody =
                Map.of("customer.age", 25, "customer.salary", 40000, "customer.department", "Sales", "customer.status",
                       "Inactive", "customer.joinDate", "2024-01-01");

        boolean result = evaluator.evaluate(expression, jsonBody);

        assertThat(result).isFalse();
    }
}