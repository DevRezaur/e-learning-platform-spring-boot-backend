package com.devrezaur.course.management.service.service;

import com.devrezaur.course.management.service.model.PaymentInfo;
import com.devrezaur.course.management.service.repository.PaymentInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentInfoRepository paymentInfoRepository;

    public PaymentService(PaymentInfoRepository paymentInfoRepository) {
        this.paymentInfoRepository = paymentInfoRepository;
    }

    public void savePaymentInfo(PaymentInfo paymentInfo) throws Exception {
        PaymentInfo existingPaymentInfo = paymentInfoRepository.findByTrxId(paymentInfo.getTrxId());
        if (existingPaymentInfo != null) {
            throw new Exception("Payment with transaction id - " + paymentInfo.getTrxId() + " already exists!");
        }
        paymentInfo.setStatus("IN_REVIEW");
        paymentInfoRepository.save(paymentInfo);
    }
}
