package co.leapwise.assignments.expression_evaluator.application.expression.representation;

import co.leapwise.assignments.expression_evaluator.domain.error.ApplicationError;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueParserTest {

    @Test
    void parseString_ShouldReturnString() {
        assertThat(ValueParser.parseValueToItsObject("\"hello\"")).isEqualTo("hello");
        assertThat(ValueParser.parseValueToItsObject("'world'")).isEqualTo("world");
    }

    @Test
    void parseInvalidString_ShouldThrowException() {
        assertThatThrownBy(() -> ValueParser.parseValueToItsObject("\"hello"))
                .isInstanceOf(ApplicationError.class)
                .hasMessageContaining("invalid string value");
    }

    @Test
    void parseBoolean_ShouldReturnBoolean() {
        assertThat(ValueParser.parseValueToItsObject("true")).isEqualTo(true);
        assertThat(ValueParser.parseValueToItsObject("false")).isEqualTo(false);
    }

    @Test
    void parseNull_ShouldReturnNull() {
        assertThat(ValueParser.parseValueToItsObject("null")).isNull();
    }

    @Test
    void parseDate_ShouldReturnLocalDate() {
        assertThat(ValueParser.parseValueToItsObject("2023-01-01")).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(ValueParser.parseValueToItsObject("2023/01/01")).isEqualTo(LocalDate.of(2023, 1, 1));
    }

    @Test
    void parseNumber_ShouldReturnNumber() {
        assertThat(ValueParser.parseValueToItsObject("123")).isEqualTo(123);
        assertThat(ValueParser.parseValueToItsObject("1587743654368543687")).isEqualTo(1587743654368543687L);
        assertThat(ValueParser.parseValueToItsObject("123.45")).isEqualTo(123.45);
    }
}