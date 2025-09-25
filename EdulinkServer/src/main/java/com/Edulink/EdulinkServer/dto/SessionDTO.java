package com.Edulink.EdulinkServer.dto;

import com.Edulink.EdulinkServer.model.Session;

import java.time.LocalDateTime;

public class SessionDTO {
    private  boolean allowAnyoneToJoin;
    private Long sessionId;
    private String topic;
    private String status;
    private int durationInMinutes;
    private String startTime;
    private String endTime;

    public SessionDTO(boolean allowAnyoneToJoin) {
        this.allowAnyoneToJoin = allowAnyoneToJoin;
    }

    // Only these two fields from User (creator)
    private String creatorFirstName;
    private String creatorLastName;

    // ✅ Constructor that matches JPQL query
    public SessionDTO(Long sessionId, String topic, String status,
                      int durationInMinutes, LocalDateTime startTime,
                      LocalDateTime endTime, boolean allowAnyoneToJoin,
                      String creatorFirstName, String creatorLastName) {
        this.sessionId = sessionId;
        this.topic = topic;
        this.status = status;
        this.durationInMinutes = durationInMinutes;
        this.startTime = String.valueOf(startTime);
        this.endTime = String.valueOf(endTime);
        this.allowAnyoneToJoin = allowAnyoneToJoin;
        this.creatorFirstName = creatorFirstName;
        this.creatorLastName = creatorLastName;
    }



    public SessionDTO() {

    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreatorLastName() {
        return creatorLastName;
    }

    public void setCreatorLastName(String creatorLastName) {
        this.creatorLastName = creatorLastName;
    }

    public String getCreatorFirstName() {
        return creatorFirstName;
    }

    public void setCreatorFirstName(String creatorFirstName) {
        this.creatorFirstName = creatorFirstName;
    }
}
