package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.StudentInfo;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public Classroom findClassRoom (Long classroomId){
        return classRepository.findById(classroomId).orElseThrow(()->new RuntimeException("Class Room Not Found"));
    }

    public Map<String ,String> initializePayment(StudentInfo studentInfo, Long ownerId, int amount){
        String url = "https://api.paystack.co/transaction/initialize";

        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Classroom Owner Not Found"));





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
        metadata.put("classroomId", owner);
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

    private static final String PAYSTACK_SUBACCOUNT_URL = "https://api.paystack.co/subaccount";

    public String createSubAccount(String businessName, String bankCode , String accountNumber , int percentageCharge){
        RestTemplate restTemplate1 = new RestTemplate();

        // request body
        Map<String , Object > request = new HashMap<>();
        request.put("businessName", businessName);
        request.put("bankCode", bankCode);
        request.put("accountNumber", accountNumber);
        request.put("percentageCharge", percentageCharge);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(payStackSecret);

        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(request, httpHeaders);

        ResponseEntity<Map> response = restTemplate1.postForEntity(PAYSTACK_SUBACCOUNT_URL, entity, Map.class);
        if (response.getStatusCode() == HttpStatus.OK && (Boolean) response.getBody().get("status")) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            return (String) data.get("subaccount_code"); // e.g. ACCT_123xyz
        } else {
            throw new RuntimeException("Failed to create subaccount: " + response.getBody());
        }
    }
}
