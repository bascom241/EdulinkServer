package com.Edulink.EdulinkServer.service;


import com.Edulink.EdulinkServer.model.BlacklistedToken;
import com.Edulink.EdulinkServer.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlacklistedTokenService {


    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    public void blacklistToken(String token , LocalDateTime expiry){
        blacklistedTokenRepository.save(new BlacklistedToken(token, expiry));
    }

    public boolean isTokenBlacklisted(String token){
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }
}
