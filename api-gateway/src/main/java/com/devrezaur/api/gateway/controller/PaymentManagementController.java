package com.devrezaur.api.gateway.controller;

import com.devrezaur.api.gateway.service.PaymentAPIService;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/payment-management-api")
public class PaymentManagementController {

    private final PaymentAPIService paymentAPIService;

    public PaymentManagementController(PaymentAPIService paymentAPIService) {
        this.paymentAPIService = paymentAPIService;
    }

    @GetMapping
    public ResponseEntity<CustomHttpResponse> getAllPaymentInfo(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                                @RequestParam @Nullable Integer pageNumber,
                                                                @RequestParam @Nullable Integer limit) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, paymentAPIService.getAllPaymentInfo(pageNumber,
                limit, accessToken));
    }

    @PostMapping
    public ResponseEntity<CustomHttpResponse> savePaymentInfo(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                              @RequestBody Map<String, Object> paymentInfo) {
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, paymentAPIService.savePaymentInfo(paymentInfo,
                accessToken));
    }

    @PostMapping("/approval")
    public ResponseEntity<CustomHttpResponse> approvePayment(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                                             @RequestBody Map<String, String> paymentStatusMap) {
        String trxId = paymentStatusMap.get("trxId");
        String status = paymentStatusMap.get("status");
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, paymentAPIService.updatePaymentStatus(trxId, status,
                accessToken));
    }
}
