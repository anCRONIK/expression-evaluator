package co.leapwise.assignments.expression_evaluator.e2e.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions {
    //only tests for create expression just as an example, could also create for evaluate expression and test caching

    @Autowired
    private TestRestTemplate restTemplate; //we could use RestAssured instead or any other library which would call the API
    // and we can create acceptance, regression tests to be called against deployed application, just need to set the base url

    private ResponseEntity<String> response;

    @Given("I create an expression with name {string} and expression {string}")
    public void iCreateAnExpressionWithNameAndExpression(String name, String expression) {
        String requestJson = String.format("""
                {
                  "name": "%s",
                  "expression": "%s"
                }""", name, expression);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        response = restTemplate.postForEntity("/expression", entity, String.class);
    }

    @Then("the response should contain a valid expressionId")
    public void theResponseShouldContainAValidExpressionId() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("\"expressionId\":\"");
    }

}
