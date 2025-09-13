package com.Edulink.EdulinkServer.service;

import org.springframework.stereotype.Service;

@Service
public class InviteLinkService {


    private final String BASE_URL = "http://localhost:5173/join-class";

    public String generateInviteLink(Long classId){
        return BASE_URL + "?classId=" + classId;
    }
}
