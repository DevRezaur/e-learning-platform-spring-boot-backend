package com.devrezaur.api.gateway.service;

import com.devrezaur.common.module.model.CustomHttpRequest;
import com.devrezaur.common.module.util.HttpCallLogic;
import com.devrezaur.common.module.util.RequestBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.devrezaur.common.module.constant.CommonConstant.AUTHORIZATION_HEADER;
import static com.devrezaur.common.module.constant.CommonConstant.COURSE_PAYMENT_API_BASE_URL;

@Service
public class PaymentAPIService {

    private final HttpCallLogic httpCallLogic;

    public PaymentAPIService(HttpCallLogic httpCallLogic) {
        this.httpCallLogic = httpCallLogic;
    }

    public Map<String, Object> getAllPaymentInfo(Integer pageNumber, Integer limit, String accessToken) {
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        Map<String, String> queryParameterMap = new HashMap<>();
        if (pageNumber != null) {
            queryParameterMap.put("pageNumber", pageNumber.toString());
        }
        if (limit != null) {
            queryParameterMap.put("limit", limit.toString());
        }
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.GET, COURSE_PAYMENT_API_BASE_URL,
                headerParameterMap, queryParameterMap, null);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> savePaymentInfo(Map<String, Object> paymentInfo, String accessToken) {
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, COURSE_PAYMENT_API_BASE_URL,
                headerParameterMap, null, paymentInfo);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }

    public Map<String, Object> updatePaymentStatus(String trxId, String status, String accessToken) {
        String url = COURSE_PAYMENT_API_BASE_URL + "/approval";
        Map<String, String> headerParameterMap = Map.of(AUTHORIZATION_HEADER, accessToken);
        Map<String, Object> bodyParameterMap = Map.of("trxId", trxId, "status", status);
        CustomHttpRequest customHttpRequest = RequestBuilder.buildRequest(HttpMethod.POST, url, headerParameterMap,
                null, bodyParameterMap);
        return httpCallLogic.getHttpResponseWithException(customHttpRequest);
    }
}
