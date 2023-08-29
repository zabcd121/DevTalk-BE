package com.devtalk.consultation.consultationservice.consultation.adapter.in.web;

import com.devtalk.consultation.consultationservice.consultation.adapter.in.web.dto.ConsultationInput;
import com.devtalk.consultation.consultationservice.consultation.application.port.in.AcceptConsultationUseCase;
import com.devtalk.consultation.consultationservice.consultation.application.port.in.CancelConsultationUseCase;
import com.devtalk.consultation.consultationservice.consultation.application.port.in.SearchConsultationUseCase;
import com.devtalk.consultation.consultationservice.consultation.application.port.in.dto.ConsultationRes;
import com.devtalk.consultation.consultationservice.global.vo.SuccessCode;
import com.devtalk.consultation.consultationservice.global.vo.SuccessResponse;
import com.devtalk.consultation.consultationservice.global.vo.SuccessResponseWithoutResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.devtalk.consultation.consultationservice.consultation.adapter.in.web.dto.ConsultationInput.*;
import static com.devtalk.consultation.consultationservice.consultation.application.port.in.dto.ConsultationRes.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsultantApiController {

    private final AcceptConsultationUseCase acceptConsultationUseCase;
    private final CancelConsultationUseCase cancelUseCase;
    private final SearchConsultationUseCase searchUseCase;

    @Operation(summary = "Get thing", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponseWithoutResult.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
    @DeleteMapping("/v1/consultants/{consultantId}/consultations/{consultationId}")
    public ResponseEntity<?> cancelConsultationByConsultant(@RequestBody @Validated CancellationOfConsultantInput cancellationInput,
                                                            @PathVariable Long consultantId,
                                                            @PathVariable Long consultationId) {
        cancelUseCase.cancelByConsultant(cancellationInput.toReq(consultantId, consultationId));
        return SuccessResponseWithoutResult.toResponseEntity(SuccessCode.CONSULTATION_CONSULTANT_CANCEL_SUCCESS);
    }

    @Operation(summary = "Get thing", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponseWithoutResult.class)))
    })
    @PostMapping("/v1/consultants/{consultantId}/consultations/{consultationId}")
    public ResponseEntity<?> acceptConsultation(@PathVariable Long consultantId,
                                                @PathVariable Long consultationId) {
        acceptConsultationUseCase.acceptConsultation(consultantId, consultationId);
        return SuccessResponseWithoutResult.toResponseEntity(SuccessCode.CONSULTANT_CONSULTATION_ACCEPT_SUCCESS);
    }

    @Operation(summary = "Get thing", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CancellationReasonRes.class)))
    })
    @GetMapping("/v1/consultants/{consultantId}/canceled-consultations/{consultationId}")
    public ResponseEntity<SuccessResponse> searchCanceledConsultationByConsultant(@PathVariable Long consultantId,
                                                                                  @PathVariable Long consultationId) {
        return SuccessResponse.toResponseEntity(SuccessCode.CONSULTER_CANCELED_CONSULTATION_SEARCH_SUCCESS,
                searchUseCase.searchCanceledConsultationDetailsByConsultant(consultantId, consultationId));
    }

    @Operation(summary = "Get thing", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = ConsultationSearchRes.class)))
    })
    @GetMapping("/v1/consultants/{consultantId}/consultations")
    public ResponseEntity<SuccessResponse> searchConsultationByConsultant(@PathVariable Long consultantId) {
        return SuccessResponse.toResponseEntity(SuccessCode.CONSULTER_CANCELED_CONSULTATION_SEARCH_SUCCESS,
                searchUseCase.searchConsultationListByConsultant(consultantId));
    }

    @Operation(summary = "Get thing", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = ReviewSearchRes.class)))
    })
    @GetMapping("/v1/consultants/{consultantId}/reviews")
    public ResponseEntity<SuccessResponse> searchReviewsByConsultant(@PathVariable Long consultantId) {
        return SuccessResponse.toResponseEntity(SuccessCode.CONSULTANT_REVIEW_SEARCH_SUCCESS,
                searchUseCase.searchReviewByConsultant(consultantId));
    }
}
