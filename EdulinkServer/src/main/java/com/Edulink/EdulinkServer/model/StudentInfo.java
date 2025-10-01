package com.Edulink.EdulinkServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "studentInfos")
public class StudentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Email(message = "Invalid email Format")
    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Column(insertable = false, updatable = false)
    private String fullName; // derived from firstName + lastName

    @Column(name = "is_student_in_a_session")
    private boolean isStudentInASession = false;

    private String joinDate;
    private String lastActive;
    private Double attendanceRate;
    private String status;        // active | inactive | pending
    private Double progress;

    @ManyToMany
    @JoinTable(
            name = "student_classrooms",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "classroom_id")
    )
    @JsonIgnore
    private List<Classroom> classrooms = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "student_sessions",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "session_id")
    )
    private List<Session> sessions = new ArrayList<>();

    // === Getters & Setters ===
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFullName();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFullName();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    // no setter for fullName → it’s automatically generated
    private void updateFullName() {
        if (this.firstName != null && this.lastName != null) {
            this.fullName = this.firstName + " " + this.lastName;
        } else if (this.firstName != null) {
            this.fullName = this.firstName;
        } else {
            this.fullName = this.lastName;
        }
    }

    public boolean isStudentInASession() {
        return isStudentInASession;
    }

    public void setStudentInASession(boolean studentInASession) {
        isStudentInASession = studentInASession;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public Double getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(Double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
