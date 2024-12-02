package co.leapwise.assignments.expression_evaluator.web.mapper;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionRequest;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExpressionMapperTest {

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
        assertThat(response.expressionId()).isEqualTo(expressionId);
    }
}