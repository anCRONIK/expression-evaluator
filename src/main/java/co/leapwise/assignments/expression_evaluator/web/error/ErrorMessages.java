package co.leapwise.assignments.expression_evaluator.web.error;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "error")
@Setter
@Slf4j
public class ErrorMessages {

    private Map<String, String> messages;

    public String getMessageDescription(String errorCode) {
        return messages.getOrDefault(errorCode, "No description for code " + errorCode);
    }
}