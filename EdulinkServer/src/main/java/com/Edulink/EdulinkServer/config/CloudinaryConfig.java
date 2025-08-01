package com.Edulink.EdulinkServer.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {


    @Value("${cloud-name}")
    private String cloudName;

    @Value("${cloud-key}")
    private String cloudkey;

    @Value("${cloud}")
    private String cloudSecret;


    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();

        config.put("cloud-name", cloudName);
        config.put("cloud-key", cloudkey);
        config.put("cloud-secret", cloudSecret);

        return new Cloudinary();
    }


}
