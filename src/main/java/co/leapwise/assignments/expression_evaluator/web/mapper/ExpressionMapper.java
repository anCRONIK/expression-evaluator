package co.leapwise.assignments.expression_evaluator.web.mapper;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionRequest;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionResponse;
import org.springframework.stereotype.Component;

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
}
