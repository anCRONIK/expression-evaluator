package co.leapwise.assignments.expression_evaluator.web.controller;

import co.leapwise.assignments.expression_evaluator.application.dto.CreateExpressionResponseDTO;
import co.leapwise.assignments.expression_evaluator.application.service.ExpressionApplicationService;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionRequest;
import co.leapwise.assignments.expression_evaluator.web.dto.CreateExpressionResponse;
import co.leapwise.assignments.expression_evaluator.web.dto.ErrorResponse;
import co.leapwise.assignments.expression_evaluator.web.dto.EvaluationResponse;
import co.leapwise.assignments.expression_evaluator.web.mapper.ExpressionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
public class ExpressionController {

    private final ExpressionApplicationService expressionApplicationService;
    private final ExpressionMapper expressionMapper;

    @Operation(summary = "Store expression")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expression is stored",
                         content = {@Content(mediaType = "application/json",
                                             schema = @Schema(implementation = CreateExpressionResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                         content = {@Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error",
                         content = {@Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping(path = "/expression", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateExpressionResponse> createExpression(@RequestBody
                                                                     CreateExpressionRequest createExpressionRequest) {
        CreateExpressionResponseDTO response = expressionApplicationService.createExpression(
                expressionMapper.mapCreateRequestToDTO(createExpressionRequest));
        return ResponseEntity.status(CREATED)
                             .body(expressionMapper.mapCreateResponseDTOToResponse(response));
    }

    @Operation(summary = "Evaluate expression")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expression is evaluated",
                         content = {@Content(mediaType = "application/json",
                                             schema = @Schema(implementation = EvaluationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                         content = {@Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Expression not found by id",
                         content = {@Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error",
                         content = {@Content(mediaType = "application/json",
                                             schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping(path = "/expression/{expressionId}", consumes = APPLICATION_JSON_VALUE,
                 produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<EvaluationResponse> evaluateExpression(
            @PathVariable String expressionId,
            @RequestBody
            Map<String, Object> jsonBody) {
        boolean result = expressionApplicationService.evaluate(expressionMapper.mapEvaluateRequestToDTO(expressionId, jsonBody));
        return ResponseEntity.status(OK)
                             .body(EvaluationResponse.builder().result(result).build());
    }
}
