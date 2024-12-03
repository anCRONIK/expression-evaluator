package co.leapwise.assignments.expression_evaluator.e2e;

import co.leapwise.assignments.expression_evaluator.ExpressionEvaluatorApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "co.leapwise.assignments.expression_evaluator")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = ExpressionEvaluatorApplication.class)
@Tag("e2eTest")
@ActiveProfiles("integration-test")
public class CucumberTestRunner {

    @BeforeAll
    static void init(@Autowired Flyway flyway) {
        flyway.migrate();
    }

    @AfterAll
    static void cleanup(@Autowired Flyway flyway) {
        flyway.clean();
    }
}