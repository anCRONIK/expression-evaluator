package co.leapwise.assignments.expression_evaluator.web.mapper;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.EvaluateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionRequest;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExpressionMapper {

    public CreateExpressionDTO mapCreateRequestToDTO(CreateExpressionRequest request) {
        return CreateExpressionDTO.builder()
                                  .expression(request.expression())
                                  .name(request.name())
                                  .build();
    }

    public CreateExpressionResponse mapCreateResponseDTOToResponse(CreateExpressionResponseDTO response) {
        return CreateExpressionResponse.builder()
                                       .expressionId(response.expressionId().toString())
                                       .build();
    }

    public EvaluateExpressionDTO mapEvaluateRequestToDTO(String expressionId, Map<String, Object> jsonBody) {
        return EvaluateExpressionDTO.builder().expressionId(expressionId).jsonBody(jsonBody).build();
    }
}
