package co.leapwise.assignments.expression_evaluator.web.controller;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionDTO;
import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.application.service.ExpressionApplicationService;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionRequest;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionResponse;
import co.leapwise.assignments.expression_evaluator.web.mapper.ExpressionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ExpressionControllerTest {

    @Mock
    private ExpressionApplicationService expressionApplicationService;

    @Mock
    private ExpressionMapper expressionMapper;

    @InjectMocks
    private ExpressionController expressionController;

    private CreateExpressionRequest createExpressionRequest;
    private CreateExpressionResponseDTO createExpressionResponseDTO;
    private CreateExpressionResponse createExpressionResponse;
    private CreateExpressionDTO createExpressionDTO;

    @BeforeEach
    void setUp() {
        createExpressionRequest = new CreateExpressionRequest("Test Expression", "(a == b)");
        createExpressionDTO = new CreateExpressionDTO("Test Expression", "(a == b)");
        createExpressionResponseDTO = new CreateExpressionResponseDTO(UUID.randomUUID());
        createExpressionResponse = new CreateExpressionResponse(createExpressionResponseDTO.expressionId().toString());
    }

    @Test
    void createExpression_ValidRequest_CallApplicationService() {
        given(expressionMapper.mapCreateRequestToDTO(createExpressionRequest)).willReturn(createExpressionDTO);
        given(expressionApplicationService.createExpression(createExpressionDTO)).willReturn(createExpressionResponseDTO);
        given(expressionMapper.mapCreateResponseDTOToResponse(createExpressionResponseDTO)).willReturn(createExpressionResponse);

        ResponseEntity<CreateExpressionResponse> responseEntity = expressionController.createExpression(createExpressionRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(createExpressionResponse);
        then(expressionMapper).should().mapCreateRequestToDTO(createExpressionRequest);
        then(expressionApplicationService).should().createExpression(createExpressionDTO);
        then(expressionMapper).should().mapCreateResponseDTOToResponse(createExpressionResponseDTO);
    }
}