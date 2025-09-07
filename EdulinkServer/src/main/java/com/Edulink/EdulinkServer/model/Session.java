package com.Edulink.EdulinkServer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")

public class Session {

    public Session() {
    }

    public Session(Long sessionId, String topic, int durationInMinutes, String status, LocalDateTime startTime, LocalDateTime endTime, boolean allowAnyoneToJoin, User creator, Classroom classroom) {
        this.sessionId = sessionId;
        this.topic = topic;
        this.durationInMinutes = durationInMinutes;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allowAnyoneToJoin = allowAnyoneToJoin;
        this.creator = creator;
        this.classroom = classroom;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    private String topic;

    private String status;

    private int durationInMinutes;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    // If true, any user can join the session
    private boolean allowAnyoneToJoin = false;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = true)

    private Classroom classroom;

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

    public boolean isAllowAnyoneToJoin() {
        return allowAnyoneToJoin;
    }

    public void setAllowAnyoneToJoin(boolean allowAnyoneToJoin) {
        this.allowAnyoneToJoin = allowAnyoneToJoin;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
}


