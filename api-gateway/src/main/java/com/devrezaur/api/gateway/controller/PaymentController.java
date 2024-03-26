package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.PaymentAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/payment-api")
public class PaymentController {

    private final PaymentAPIService paymentAPIService;

    public PaymentController(PaymentAPIService paymentAPIService) {
        this.paymentAPIService = paymentAPIService;
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getAllPayments(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                             @RequestParam @Nullable Integer pageNumber,
                                                             @RequestParam @Nullable Integer limit) {
        List<Map<String, Object>> paymentInfoList;
        try {
            paymentInfoList = paymentAPIService.getAllPaymentInfo(pageNumber, limit, accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch payment info! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("paymentInfoList", paymentInfoList));
    }

    @PostMapping
    public ResponseEntity<CustomHttpResponse> savePaymentInfo(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                              @RequestBody Map<String, Object> paymentInfo) {
        String message;
        try {
            message = paymentAPIService.savePaymentInfo(paymentInfo, accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to add payment info! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("message", message));
    }

    @PostMapping("/approval")
    public ResponseEntity<CustomHttpResponse> updatePaymentStatus(
            @RequestHeader(AUTHORIZATION_HEADER) String accessToken,
            @RequestBody Map<String, String> paymentStatusMap) {
        String message;
        try {
            message = paymentAPIService.updatePaymentStatus(paymentStatusMap.get("trxId"),
                    paymentStatusMap.get("status"), accessToken);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.EXPECTATION_FAILED, "417",
                    "Failed to update payment status! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("message", message));
    }
}
