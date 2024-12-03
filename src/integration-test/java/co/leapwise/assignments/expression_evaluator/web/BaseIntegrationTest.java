package co.leapwise.assignments.expression_evaluator.web;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@Tag("integrationTest")
@ActiveProfiles("integration-test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    static void init(@Autowired Flyway flyway) {
        flyway.migrate();
    }

    @AfterAll
    static void cleanup(@Autowired Flyway flyway) {
        flyway.clean();
    }

}
