package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.model.StudentInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayStackService {

    @Value("${paystack.secret.key}")
    private String payStackSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String ,String> initializePayment(StudentInfo studentInfo, Long classroomId, int amount){
        String url = "https://api.paystack.co/transaction/initialize";



//        Map<String, Object > body = Map.of(
//                "email" , studentInfo.getEmail(),
//                "amount" , amount,
//                "callback_url" , "http://localhost:5173/initialize/success"
//        );


        Map<String, Object> body = new HashMap<>();

        body.put("email", studentInfo.getEmail());
        body.put("amount", amount);
        body.put("callback_url" , "http://localhost:5173/initialize/success");

        Map<String, Object> metadata =  new HashMap<>();
        metadata.put("studentInfo", studentInfo.getFullName());
        metadata.put("classroomId", classroomId);
        body.put("metadata", metadata);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(payStackSecret);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, entity, Map.class);

        Map<String, Object> data = (Map<String, Object>) responseEntity.getBody().get("data");

        Map<String, String > result = new HashMap<>();
        result.put("authUrl", (String) data.get("authorization_url"));
        result.put("reference",(String) data.get("reference"));

        return result;

    }
}
