package com.example.payments_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class MpesaService {

    @Value("${mpesa.api.key}")
    private String apiKey;

    @Value("${mpesa.service.code}")
    private String serviceCode;

    private final RestTemplate restTemplate;

    public MpesaService() {
        restTemplate = new RestTemplate();
    }

    public Map<String, Object> enviarPedidoPagamento(
            BigDecimal amount,
            String phoneNumber,
            String reference) {

        String url =
                "https://sandbox.api.mpesa.vm.co.mz/v1/c2bPayment/singleStage/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, String> body = new HashMap<>();

        body.put("input_TransactionReference", reference);
        body.put("input_CustomerMSISDN", phoneNumber);
        body.put("input_Amount", amount.toString());
        body.put("input_ServiceProviderCode", serviceCode);
        body.put("input_ThirdPartyReference", reference);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            url,
                            request,
                            Map.class
                    );

            return response.getBody();

        } catch (Exception e) {

            Map<String, Object> error = new HashMap<>();

            error.put("status", "FAIL");
            error.put("message", "Erro ao comunicar com M-Pesa");

            return error;
        }
    }
}