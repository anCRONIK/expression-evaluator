package co.leapwise.assignments.expression_evaluator.web.controller;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.application.service.ExpressionApplicationService;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionRequest;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionResponse;
import co.leapwise.assignments.expression_evaluator.web.mapper.ExpressionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/expression")
public class ExpressionController {

    private final ExpressionApplicationService expressionApplicationService;
    private final ExpressionMapper expressionMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateExpressionResponse> createExpression(@RequestBody
                                                                     CreateExpressionRequest createExpressionRequest) {
        CreateExpressionResponseDTO response = expressionApplicationService.createExpression(
                expressionMapper.mapCreateRequestToDTO(createExpressionRequest));
        return ResponseEntity.status(CREATED)
                             .body(expressionMapper.mapCreateResponseDTOToResponse(response));
    }
}
