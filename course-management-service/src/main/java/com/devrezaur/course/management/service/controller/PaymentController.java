package com.devrezaur.course.management.service.controller;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.ResponseBuilder;
import com.devrezaur.course.management.service.model.PaymentInfo;
import com.devrezaur.course.management.service.service.EnrollmentService;
import com.devrezaur.course.management.service.service.PaymentService;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.MESSAGE;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final String IN_REVIEW_STATUS = "IN-REVIEW";

    private final PaymentService paymentService;
    private final EnrollmentService enrollmentService;

    public PaymentController(PaymentService paymentService, EnrollmentService enrollmentService) {
        this.paymentService = paymentService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomHttpResponse> savePaymentInfo(@RequestBody PaymentInfo paymentInfo) {
        try {
            paymentInfo.setStatus(IN_REVIEW_STATUS);
            paymentService.savePaymentInfo(paymentInfo);
            enrollmentService.enrollToCourse(paymentInfo.getCourseId(), paymentInfo.getUserId(),
                    paymentInfo.getStatus());
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to save payment info! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.CREATED, Map.of(MESSAGE,
                "Successfully saved payment info"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomHttpResponse> getAllPaymentInfo(@RequestParam @Nullable Integer pageNumber,
                                                                @RequestParam @Nullable Integer limit) {
        List<PaymentInfo> paymentInfoList;
        try {
            paymentInfoList = paymentService.getAllPaymentInfo(pageNumber, limit);
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.BAD_REQUEST, "400",
                    "Failed to fetch payment info! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of("paymentInfoList", paymentInfoList));
    }

    @PostMapping("/approval")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomHttpResponse> updatePaymentStatus(@RequestBody PaymentInfo paymentInfo) {
        try {
            paymentService.updatePaymentStatus(paymentInfo.getTrxId(), paymentInfo.getStatus());
            enrollmentService.enrollToCourse(paymentInfo.getCourseId(), paymentInfo.getUserId(),
                    paymentInfo.getStatus());
        } catch (Exception ex) {
            return ResponseBuilder.buildFailureResponse(HttpStatus.EXPECTATION_FAILED, "417",
                    "Failed to update payment status! Reason: " + ex.getMessage());
        }
        return ResponseBuilder.buildSuccessResponse(HttpStatus.OK, Map.of(MESSAGE,
                "Successfully updated payment status"));
    }
}
