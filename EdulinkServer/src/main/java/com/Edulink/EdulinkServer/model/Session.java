package com.Edulink.EdulinkServer.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")

public class Session {

    public Session() {
    }

    public Session(Long sessionId, String topic, int durationInMinutes, String status, LocalDateTime startTime, LocalDateTime endTime, boolean allowAnyoneToJoin, User creator, Classroom classroom, List<StudentInfo> studentInfo) {
        this.sessionId = sessionId;
        this.topic = topic;
        this.durationInMinutes = durationInMinutes;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allowAnyoneToJoin = allowAnyoneToJoin;
        this.creator = creator;
        this.classroom = classroom;
        this.students = studentInfo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToMany(mappedBy = "sessions")
    private List<StudentInfo> students = new ArrayList<>();


    public List<StudentInfo> getStudents() {
        return students;
    }

    public void setStudents(List<StudentInfo> students) {
        this.students = students;
    }

    private String topic;

    private String status;

    private int durationInMinutes;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    // If true, any user can join the session
    private boolean allowAnyoneToJoin = false;


    // Class Location Method
    @Column(name = "sessionClassLink", nullable = true)
    private String sessionClassLink;

    @Column(name = "sessionClassLocation", nullable = true)
    private String sessionClassLocation;

    @Column(name = "requirePassword")
   private boolean requirePassword = false ;

    public boolean isRequirePassword() {
        return requirePassword;
    }

    public void setRequirePassword(boolean requirePassword) {
        this.requirePassword = requirePassword;
    }

    @Column(name = "sessionPassword" , nullable = true)
    private String sessionPassword;

    public String getSessionPassword() {
        return sessionPassword;
    }

    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }

    public String getSessionClassLink() {
        return sessionClassLink;
    }

    public void setSessionClassLink(String sessionClassLink) {
        this.sessionClassLink = sessionClassLink;
    }

    public String getSessionClassLocation() {
        return sessionClassLocation;
    }

    public void setSessionClassLocation(String sessionClassLocation) {
        this.sessionClassLocation = sessionClassLocation;
    }



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


