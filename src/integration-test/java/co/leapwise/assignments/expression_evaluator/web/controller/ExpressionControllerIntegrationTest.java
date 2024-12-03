package co.leapwise.assignments.expression_evaluator.web.controller;

import co.leapwise.assignments.expression_evaluator.domain.model.Expression;
import co.leapwise.assignments.expression_evaluator.infrastructure.repository.ExpressionRepository;
import co.leapwise.assignments.expression_evaluator.web.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExpressionControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ExpressionRepository expressionRepository;

    @Test
    void createExpression_ValidRequest_ShouldReturnValidResponse() throws Exception {
        String requestJson = """
                {
                  "name": "Complex expression",
                  "expression": "(customer.firstName == 'John' && customer.salary < 100) || customer.address.city == 'Washington'"
                }""";

        mockMvc.perform(post("/expression")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.expressionId").exists())
               .andExpect(jsonPath("$.expressionId").isString());
    }

    @Test
    void createExpression_DuplicateName_ShouldReturnBadRequest() throws Exception {
        String requestJson = """
                {
                  "name": "Duplicate expression",
                  "expression": "customer.age >= 30"
                }""";

        mockMvc.perform(post("/expression")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
               .andExpect(status().isCreated());

        mockMvc.perform(post("/expression")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
               .andExpect(status().isConflict())
               .andExpect(jsonPath("$.errorCode").value("DATABASE_ERROR"))
               .andExpect(jsonPath("$.description").value(
                       "Could not store item. Please check if the expression with that name is already stored."));
        ;
    }

    @Test
    void createExpression_InvalidExpression_ShouldReturnBadRequest() throws Exception {
        String requestJson = """
                {
                  "name": "Invalid expression",
                  "expression": "customer.age >="
                }""";

        mockMvc.perform(post("/expression")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errorCode").value("INVALID_EXPRESSION"))
               .andExpect(jsonPath("$.description").value("Given expression is not valid. Please check syntax."));
        ;
    }

    @Test
    void createExpression_DuplicateExpressionDifferentName_ShouldReturnValidResponse() throws Exception {
        String requestJson1 = """
                {
                  "name": "Expression 1",
                  "expression": "customer.age >= 30"
                }""";

        String requestJson2 = """
                {
                  "name": "Expression 2",
                  "expression": "customer.age >= 30"
                }""";

        mockMvc.perform(post("/expression")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson1))
               .andExpect(status().isCreated());

        mockMvc.perform(post("/expression")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson2))
               .andExpect(status().isCreated());
    }

    @Test
    void evaluateExpression_InvalidId_ShouldReturnNotFound() throws Exception {
        String requestJson = """
                {
                  "variables": {}
                }""";

        mockMvc.perform(post("/expression/invalid-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST"))
               .andExpect(jsonPath("$.description").value("must be a valid UUID"));
    }

    @Test
    void evaluateExpression_NoExpressionFound_ShouldReturnNotFound() throws Exception {
        String requestJson = """
                {
                  "variables": {}
                }""";

        mockMvc.perform(post("/expression/a657aac2-f108-44f4-b8c5-31ce5a38de73")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.errorCode").value("EXPRESSION_NOT_FOUND"))
               .andExpect(jsonPath("$.description").value(
                       "Expression with given ID is not found. [a657aac2-f108-44f4-b8c5-31ce5a38de73]"));
    }

    @Test
    void createAndEvaluateExpression_ValidRequest_ShouldReturnValidResponse() throws Exception {
        Expression expression = new Expression(1L, UUID.randomUUID(), "Test Expression", "customer.age >= 30");
        expressionRepository.save(expression);

        String evaluateRequestJson = """
                {
                  "customer": {
                    "age": 35
                  }
                }""";

        mockMvc.perform(post("/expression/" + expression.getIdentifier())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(evaluateRequestJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    void createAndEvaluateExpression_ValidRequest_FullWorkflow() throws Exception {
        String createRequestJson = """
                {
                  "name": "Complex expression 456789765t",
                  "expression": "(customer.firstName == \\"JOHN\\" && customer.salary < 100) || (customer.address != null && customer.address.city == 'Washington')"
                }""";

        String evaluateRequestJson = """
                {
                  "customer": {
                    "firstName": "JOHN",
                    "lastName": "DOE",
                    "address": {
                      "city": "Chicago",
                      "zipCode": 1234,
                      "street": "56th",
                      "houseNumber": 2345
                    },
                    "salary": 99,
                    "type": "BUSINESS"
                  }
                }""";

        String expressionId = mockMvc.perform(post("/expression")
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(createRequestJson))
                                     .andExpect(status().isCreated())
                                     .andExpect(jsonPath("$.expressionId").exists())
                                     .andReturn()
                                     .getResponse()
                                     .getContentAsString();

        expressionId = expressionId.substring(expressionId.indexOf(":") + 2, expressionId.length() - 2);

        mockMvc.perform(post("/expression/" + expressionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(evaluateRequestJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value(true));
    }

    //TODO tests for too big expression for storing in database

    //should be written with cucumber as acceptance test, this way is too flaky, just an example what should be also
    // tested, also it would be the best to have really huge expression for that test
//    @Test
//    void createAndEvaluateExpression_ValidRequest_TryingTestForCacheable() throws Exception {
//        String createRequestJson = """
//        {
//          "name": "Complex expression cache test",
//          "expression": "(customer.firstName == \\"JOHN\\" && customer.salary < 100) || (customer.address != null
//          && customer.address.city == 'Washington')"
//        }""";
//
//        String evaluateRequestJson = """
//        {
//          "customer": {
//            "firstName": "JOHN",
//            "lastName": "DOE",
//            "address": {
//              "city": "Chicago",
//              "zipCode": 1234,
//              "street": "56th",
//              "houseNumber": 2345
//            },
//            "salary": 99,
//            "type": "BUSINESS"
//          }
//        }""";
//
//        String expressionId = mockMvc.perform(post("/expression")
//                                                      .contentType(MediaType.APPLICATION_JSON)
//                                                      .content(createRequestJson))
//                                     .andExpect(status().isCreated())
//                                     .andExpect(jsonPath("$.expressionId").exists())
//                                     .andReturn()
//                                     .getResponse()
//                                     .getContentAsString();
//
//        expressionId = expressionId.substring(expressionId.indexOf(":") + 2, expressionId.length() - 2);
//
//        // Measure response time for the first evaluation
//        long startTime = System.nanoTime();
//        mockMvc.perform(post("/expression/" + expressionId)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(evaluateRequestJson))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$.result").value(true));
//        long endTime = System.nanoTime();
//        long durationFirst = endTime - startTime;
//        System.out.println("First evaluation time: " + durationFirst + " nanoseconds");
//
//        // create second expression to "avoid" db cache
//        String secondCreateRequestJson = """
//        {
//          "name": "secondCreateRequestJson test1",
//          "expression": "(customer.firstName != \\"JOHN\\" && customer.salary < 100) || (customer.address != null
//          && customer.address.city == \\"Washington\\")"
//        }""";
//        mockMvc.perform(post("/expression")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(secondCreateRequestJson))
//               .andExpect(status().isCreated())
//               .andExpect(jsonPath("$.expressionId").exists())
//               .andReturn()
//               .getResponse()
//               .getContentAsString();
//
//        // Measure response time for the cached evaluation
//        startTime = System.nanoTime();
//        mockMvc.perform(post("/expression/" + expressionId)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(evaluateRequestJson))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$.result").value(true));
//        endTime = System.nanoTime();
//        long durationCached = endTime - startTime;
//        System.out.println("Cached evaluation time: " + durationCached + " nanoseconds");
//
//        assertThat(durationCached).isLessThan(durationFirst);
//    }
}
