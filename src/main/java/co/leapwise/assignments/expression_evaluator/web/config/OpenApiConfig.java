package co.leapwise.assignments.expression_evaluator.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Expression Evaluator API",
        version = "v1",
        description = "API documentation for the Expression Evaluator application"
    )
)
@Configuration
public class OpenApiConfig {
}
