package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;

@Service
public class PaymentService {

    private final HttpCallLogic httpCallLogic;

    public PaymentService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public String savePaymentInfo(Map<String, Object> paymentInfo, String accessToken) throws Exception {
        String url = "course-management-service/payment";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, paymentInfo);
        try {
            ResponseEntity<CustomHttpResponse> responseEntity = httpCallLogic.executeRequest(customHttpRequest);
            return (String) responseEntity.getBody().getResponseBody().get("message");
        } catch (Exception ex) {
            throw new Exception("Error occurred while calling COURSE-MANAGEMENT-SERVICE!");
        }
    }
}
