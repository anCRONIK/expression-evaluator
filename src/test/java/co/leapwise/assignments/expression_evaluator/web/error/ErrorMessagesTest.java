package co.leapwise.assignments.expression_evaluator.web.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ErrorMessagesTest {

    private final ErrorMessages errorMessages = new ErrorMessages();

    private final Map<String, String> mockMessages = new HashMap<>();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(errorMessages, "messages", mockMessages);
    }

    @Test
    void getMessageDescription_DescriptionFound_ShouldReturnIt() {
        String errorCode = "CUSTOM_ERROR";
        String expectedDescription = "This is a custom error";
        mockMessages.put(errorCode, expectedDescription);

        String actualDescription = errorMessages.getMessageDescription(errorCode);

        assertThat(actualDescription).isEqualTo(expectedDescription);
    }

    @Test
    void getMessageDescription_DescriptionNotFound_ShouldReturnDefaultErrorMessage() {
        String errorCode = "RANDOM_ERROR";
        String expectedDescription = "No description for code RANDOM_ERROR";
        mockMessages.put(errorCode, expectedDescription);

        String actualDescription = errorMessages.getMessageDescription(errorCode);

        assertThat(actualDescription).isEqualTo(expectedDescription);
    }
}
