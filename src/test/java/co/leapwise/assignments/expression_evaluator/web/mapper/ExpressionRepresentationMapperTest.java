package co.leapwise.assignments.expression_evaluator.web.mapper;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.EvaluateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionRequest;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionRepresentationMapperTest {

    private final ExpressionMapper expressionMapper = new ExpressionMapper();;

    @Test
    void mapCreateRequestToDTO_ShouldMapCorrectly() {
        String expression = "(customer.firstName == \"JOHN\")";
        String name = "Simple Expression";
        CreateExpressionRequest request = new CreateExpressionRequest(name, expression);

        CreateExpressionDTO dto = expressionMapper.mapCreateRequestToDTO(request);

        assertThat(dto).isNotNull();
        assertThat(dto.expression()).isEqualTo(expression);
        assertThat(dto.name()).isEqualTo(name);
    }

    @Test
    void mapCreateResponseDTOToResponse_ShouldMapCorrectly() {
        UUID expressionId = UUID.randomUUID();
        CreateExpressionResponseDTO responseDTO = new CreateExpressionResponseDTO(expressionId);

        CreateExpressionResponse response = expressionMapper.mapCreateResponseDTOToResponse(responseDTO);

        assertThat(response).isNotNull();
        assertThat(response.expressionId()).isEqualTo(expressionId.toString());
    }

    @Test
    void mapEvaluateRequestToDTO_ShouldMapCorrectly() {
        String expressionId = "123e4567-e89b-12d3-a456-426614174000";
        Map<String, Object> jsonBody = Map.of(
                "customer", Map.of(
                        "firstName", "John",
                        "salary", 120
                )
        );

        EvaluateExpressionDTO result = expressionMapper.mapEvaluateRequestToDTO(expressionId, jsonBody);

        assertThat(result).isNotNull();
        assertThat(result.expressionId()).isEqualTo(expressionId);
        assertThat(result.jsonBody()).isEqualTo(jsonBody);
    }
}