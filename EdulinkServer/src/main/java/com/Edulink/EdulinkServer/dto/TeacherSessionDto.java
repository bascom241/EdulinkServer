package com.Edulink.EdulinkServer.dto;


import java.time.LocalDateTime;

public class TeacherSessionDto {
    private final boolean allowAnyoneToJoin;
    private Long sessionId;
    private String topic;
    private String status;
    private int durationInMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TeacherSessionDto(boolean allowAnyoneToJoin){
        this.allowAnyoneToJoin = allowAnyoneToJoin;
    }

    private String className;

    public TeacherSessionDto(boolean allowAnyoneToJoin, Long sessionId, String topic, String status, int durationInMinutes, LocalDateTime startTime, LocalDateTime endTime, String className) {
        this.allowAnyoneToJoin = allowAnyoneToJoin;
        this.sessionId = sessionId;
        this.topic = topic;
        this.status = status;
        this.durationInMinutes = durationInMinutes;
        this.startTime = startTime;
        this.endTime = endTime;
        this.className = className;
    }

    public boolean isAllowAnyoneToJoin() {
        return allowAnyoneToJoin;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getClassroomName() {
        return className;
    }

    public void setClassroomName(String classroomName) {
        this.className = classroomName;
    }
}
