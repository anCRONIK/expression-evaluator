package co.leapwise.assignments.expression_evaluator.application.expression;

import co.leapwise.assignments.expression_evaluator.application.expression.representation.ExpressionRepresentation;
import co.leapwise.assignments.expression_evaluator.application.expression.representation.LogicalOperator;
import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionParserTest {

    private final ExpressionParser parser = new ExpressionParser();

    @Test
    void parse_NullExpression_ShouldThrowException() {
        assertThatThrownBy(() -> parser.parseInternal(null))
                .isInstanceOf(ApplicationError.class);
    }

    @Test
    void parse_InvalidExpression_ShouldThrowException() {
        String expression = "customer.salary >= 100 & customer.firstName == \"John\"";
        assertThatThrownBy(() -> parser.parseInternal(expression))
                .isInstanceOf(ApplicationError.class)
                .hasMessageContaining("INVALID_EXPRESSION");
    }

    @Test
    void parse_SimpleCondition_BooleanTrue_ShouldParseCorrectly() {
        String expression = "customer.active == true";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.active");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("==");
        assertThat(representation.condition().value()).isEqualTo(true);
    }

    @Test
    void parse_SimpleCondition_BooleanFalse_ShouldParseCorrectly() {
        String expression = "customer.active == false";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.active");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("==");
        assertThat(representation.condition().value()).isEqualTo(false);
    }

    @Test
    void parse_SimpleCondition_StringEquals_ShouldParseCorrectly() {
        String expression = "customer.name == \"John\"";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.name");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("==");
        assertThat(representation.condition().value()).isEqualTo("John");
    }

    @Test
    void parse_SimpleCondition_StringNotEquals_ShouldParseCorrectly() {
        String expression = "customer.name != \"John\"";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.name");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("!=");
        assertThat(representation.condition().value()).isEqualTo("John");
    }

    @Test
    void parse_SimpleCondition_NumberEquals_ShouldParseCorrectly() {
        String expression = "customer.age == 30";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.age");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("==");
        assertThat(representation.condition().value()).isEqualTo(30);
    }

    @Test
    void parse_SimpleCondition_NumberNotEquals_ShouldParseCorrectly() {
        String expression = "customer.age != 30";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.age");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("!=");
        assertThat(representation.condition().value()).isEqualTo(30);
    }

    @Test
    void parse_SimpleCondition_NumberGreaterThan_ShouldParseCorrectly() {
        String expression = "customer.age > 30";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.age");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo(">");
        assertThat(representation.condition().value()).isEqualTo(30);
    }

    @Test
    void parse_SimpleCondition_NumberLessThan_ShouldParseCorrectly() {
        String expression = "customer.age < 30";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.age");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("<");
        assertThat(representation.condition().value()).isEqualTo(30);
    }

    @Test
    void parse_SimpleCondition_NumberGreaterThanOrEquals_ShouldParseCorrectly() {
        String expression = "customer.age >= 30";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.age");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo(">=");
        assertThat(representation.condition().value()).isEqualTo(30);
    }

    @Test
    void parse_SimpleCondition_DoubleEquals_ShouldParseCorrectly() {
        String expression = "customer.salary == 150.5";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.salary");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("==");
        assertThat(representation.condition().value()).isEqualTo(150.5);
    }

    @Test
    void parse_DateNotEquals_ShouldParseCorrectly() {
        String expression = "customer.joinDate != \"2023-01-01\"";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.joinDate");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo("!=");
        assertThat(representation.condition().value()).isEqualTo("2023-01-01");
    }

    @Test
    void parse_DateGreaterThan_ShouldParseCorrectly() {
        String expression = "customer.joinDate > \"2023-01-01\"";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.condition().field()).isEqualTo("customer.joinDate");
        assertThat(representation.condition().operator().getSymbol()).isEqualTo(">");
        assertThat(representation.condition().value()).isEqualTo("2023-01-01");
    }

    @Test
    void parse_SimpleAndExpression_ShouldParseCorrectly() {
        String expression = "customer.salary >= 100 && customer.firstName == \"John\"";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.operator()).isEqualTo(LogicalOperator.AND);
        assertThat(representation.left().condition().field()).isEqualTo("customer.salary");
        assertThat(representation.left().condition().operator().getSymbol()).isEqualTo(">=");
        assertThat(representation.left().condition().value()).isEqualTo(100);

        assertThat(representation.right().condition().field()).isEqualTo("customer.firstName");
        assertThat(representation.right().condition().operator().getSymbol()).isEqualTo("==");
        assertThat(representation.right().condition().value()).isEqualTo("John");
    }

    @Test
    void parse_SimpleAndExpressionWithWord_ShouldParseCorrectly() {
        String expression = "customer.salary >= 100 AND customer.firstName == \"John\"";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.operator()).isEqualTo(LogicalOperator.AND);
        assertThat(representation.left().condition().field()).isEqualTo("customer.salary");
        assertThat(representation.left().condition().operator().getSymbol()).isEqualTo(">=");
        assertThat(representation.left().condition().value()).isEqualTo(100);

        assertThat(representation.right().condition().field()).isEqualTo("customer.firstName");
        assertThat(representation.right().condition().operator().getSymbol()).isEqualTo("==");
        assertThat(representation.right().condition().value()).isEqualTo("John");
    }

    @Test
    void parse_SimpleAndExpressionForValuesInArray_ShouldParseCorrectly() {
        String expression = "customer[0].salary >= 100 AND customer[0].firstName == \"John\"";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.operator()).isEqualTo(LogicalOperator.AND);
        assertThat(representation.left().condition().field()).isEqualTo("customer[0].salary");
        assertThat(representation.left().condition().operator().getSymbol()).isEqualTo(">=");
        assertThat(representation.left().condition().value()).isEqualTo(100);

        assertThat(representation.right().condition().field()).isEqualTo("customer[0].firstName");
        assertThat(representation.right().condition().operator().getSymbol()).isEqualTo("==");
        assertThat(representation.right().condition().value()).isEqualTo("John");
    }

    @Test
    void parse_ComplexExpression_ShouldParseCorrectly() {
        String expression = "(customer.firstName == \"JOHN\" && customer.salary < 100) OR (customer.address != null && customer.address.city == \"Washington\")";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.operator()).isEqualTo(LogicalOperator.OR);

        ExpressionRepresentation left = representation.left();
        assertThat(left.operator()).isEqualTo(LogicalOperator.AND);
        assertThat(left.right().condition().field()).isEqualTo("customer.salary");
        assertThat(left.right().condition().operator().getSymbol()).isEqualTo("<");
        assertThat(left.right().condition().value()).isEqualTo(100);

        assertThat(left.left().condition().field()).isEqualTo("customer.firstName");
        assertThat(left.left().condition().operator().getSymbol()).isEqualTo("==");
        assertThat(left.left().condition().value()).isEqualTo("JOHN");

        ExpressionRepresentation right = representation.right();
        assertThat(right.operator()).isEqualTo(LogicalOperator.AND);
        assertThat(right.left().condition().field()).isEqualTo("customer.address");
        assertThat(right.left().condition().operator().getSymbol()).isEqualTo("!=");
        assertThat(right.left().condition().value()).isNull();

        assertThat(right.right().condition().field()).isEqualTo("customer.address.city");
        assertThat(right.right().condition().operator().getSymbol()).isEqualTo("==");
        assertThat(right.right().condition().value()).isEqualTo("Washington");
    }

    @Test
    void parse_ComplexNestedExpression_ShouldParseCorrectly() {
        String expression = "(customer.age >= 30 AND (customer.salary > 50000 OR customer.department == \"Engineering\")) OR (customer.status == \"Active\" AND customer.joinDate <= \"2023-01-01\")";
        ExpressionRepresentation representation = parser.parseInternal(expression);

        assertThat(representation.operator()).isEqualTo(LogicalOperator.OR);

        ExpressionRepresentation left = representation.left();
        assertThat(left.operator()).isEqualTo(LogicalOperator.AND);
        assertThat(left.left().condition().field()).isEqualTo("customer.age");
        assertThat(left.left().condition().operator().getSymbol()).isEqualTo(">=");
        assertThat(left.left().condition().value()).isEqualTo(30);

        ExpressionRepresentation leftRight = left.right();
        assertThat(leftRight.operator()).isEqualTo(LogicalOperator.OR);
        assertThat(leftRight.left().condition().field()).isEqualTo("customer.salary");
        assertThat(leftRight.left().condition().operator().getSymbol()).isEqualTo(">");
        assertThat(leftRight.left().condition().value()).isEqualTo(50000);
        assertThat(leftRight.right().condition().field()).isEqualTo("customer.department");
        assertThat(leftRight.right().condition().operator().getSymbol()).isEqualTo("==");
        assertThat(leftRight.right().condition().value()).isEqualTo("Engineering");

        ExpressionRepresentation right = representation.right();
        assertThat(right.operator()).isEqualTo(LogicalOperator.AND);
        assertThat(right.left().condition().field()).isEqualTo("customer.status");
        assertThat(right.left().condition().operator().getSymbol()).isEqualTo("==");
        assertThat(right.left().condition().value()).isEqualTo("Active");
        assertThat(right.right().condition().field()).isEqualTo("customer.joinDate");
        assertThat(right.right().condition().operator().getSymbol()).isEqualTo("<=");
        assertThat(right.right().condition().value()).isEqualTo("2023-01-01");
    }
}
