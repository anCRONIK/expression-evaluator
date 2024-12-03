package co.leapwise.assignments.expression_evaluator.web.controller;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.EvaluateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.service.ExpressionApplicationService;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionRequest;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionResponse;
import co.leapwise.assignments.expression_evaluator.web.mapper.ExpressionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class ExpressionRepresentationControllerTest {

    @Mock
    private ExpressionApplicationService expressionApplicationService;

    @Mock
    private ExpressionMapper expressionMapper;

    @InjectMocks
    private ExpressionController expressionController;

    @Test
    void createExpression_ValidRequest_CallApplicationService() {
        var createExpressionRequest = new CreateExpressionRequest("Test Expression", "(a == b)");
        var createExpressionDTO = new CreateExpressionDTO("Test Expression", "(a == b)");
        var createExpressionResponseDTO = new CreateExpressionResponseDTO(UUID.randomUUID());
        var createExpressionResponse =
                new CreateExpressionResponse(createExpressionResponseDTO.expressionId().toString());
        given(expressionMapper.mapCreateRequestToDTO(createExpressionRequest)).willReturn(createExpressionDTO);
        given(expressionApplicationService.createExpression(createExpressionDTO)).willReturn(
                createExpressionResponseDTO);
        given(expressionMapper.mapCreateResponseDTOToResponse(
                createExpressionResponseDTO)).willReturn(createExpressionResponse);

        ResponseEntity<CreateExpressionResponse> responseEntity = expressionController.createExpression(
                createExpressionRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(createExpressionResponse);
        then(expressionMapper).should().mapCreateRequestToDTO(createExpressionRequest);
        then(expressionApplicationService).should().createExpression(createExpressionDTO);
        then(expressionMapper).should().mapCreateResponseDTOToResponse(createExpressionResponseDTO);
    }

    @Test
    void evaluateExpression_ValidRequest_ShouldReturnEvaluationResult() {
        String expressionId = "123e4567-e89b-12d3-a456-426614174000";
        Map<String, Object> jsonBody = Map.of("customer", Map.of("firstName", "John", "salary", 120));
        boolean evaluationResult = true;
        EvaluateExpressionDTO evaluateExpressionDTO = new EvaluateExpressionDTO(expressionId, jsonBody);
        given(expressionMapper.mapEvaluateRequestToDTO(expressionId, jsonBody)).willReturn(evaluateExpressionDTO);
        given(expressionApplicationService.evaluate(evaluateExpressionDTO)).willReturn(evaluationResult);

        var responseEntity = expressionController.evaluateExpression(expressionId, jsonBody);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().result()).isEqualTo(evaluationResult);
        then(expressionApplicationService).shouldHaveNoMoreInteractions();
        then(expressionMapper).shouldHaveNoMoreInteractions();
    }
}