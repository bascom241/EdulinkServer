package com.Edulink.EdulinkServer.dto.classroom;

import com.Edulink.EdulinkServer.dto.SessionDTO;
import com.Edulink.EdulinkServer.dto.StudentInfoDto;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.StudentInfo;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.model.embeddables.ClassMaterial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ManyToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClassroomResponseDto {
    private Long classId;
    private String className;
    private String classDescription;
    private int classroomPrice;
    private boolean isClassroomFull;
    private int classDurationInDays;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String classDeliveryModel;
    private String classLocation;
    private String targetAudience;
    private String classCategory;

    private List<ClassMaterial> resources;
    private List<ClassMaterial> assignments;
    private List<ClassMaterial> tasks;


    private String classroomOwnerFirstName;
    private Long instructorId;
    private String classroomOwnerEmail;

    public String getClassroomOwnerEmail() {
        return classroomOwnerEmail;
    }

    public void setClassroomOwnerEmail(String classroomOwnerEmail) {
        this.classroomOwnerEmail = classroomOwnerEmail;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public String getClassroomOwnerFirstName() {
        return classroomOwnerFirstName;
    }

    public void setClassroomOwnerFirstName(String classroomOwnerFirstName) {
        this.classroomOwnerFirstName = classroomOwnerFirstName;
    }

    private List<StudentInfoDto> students ;

    private List<SessionDTO> sessions;

    public List<StudentInfoDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentInfoDto> students) {
        this.students = students;
    }

    public List<SessionDTO> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionDTO> sessions) {
        this.sessions = sessions;
    }

    private int numberOfStudents;  // instead of exposing all student details
    private int numberOfSessions;
    private int numberOfQuestions;

    private boolean isSessionOngoing = false ;

    public boolean isSessionOngoing() {
        return isSessionOngoing;
    }

    public void setSessionOngoing(boolean sessionOngoing) {
        isSessionOngoing = sessionOngoing;
    }

    private String inviteLink;

    public String getInviteLink() {
        return inviteLink;
    }

    public void setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
    }

    // Getters and Setters
    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public int getClassroomPrice() {
        return classroomPrice;
    }

    public void setClassroomPrice(int classroomPrice) {
        this.classroomPrice = classroomPrice;
    }

    public boolean isClassroomFull() {
        return isClassroomFull;
    }

    public void setClassroomFull(boolean classroomFull) {
        isClassroomFull = classroomFull;
    }

    public int getClassDurationInDays() {
        return classDurationInDays;
    }

    public void setClassDurationInDays(int classDurationInDays) {
        this.classDurationInDays = classDurationInDays;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getClassDeliveryModel() {
        return classDeliveryModel;
    }

    public void setClassDeliveryModel(String classDeliveryModel) {
        this.classDeliveryModel = classDeliveryModel;
    }

    public String getClassLocation() {
        return classLocation;
    }

    public void setClassLocation(String classLocation) {
        this.classLocation = classLocation;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public String getClassCategory() {
        return classCategory;
    }

    public void setClassCategory(String classCategory) {
        this.classCategory = classCategory;
    }

    public List<ClassMaterial> getResources() {
        return resources;
    }

    public void setResources(List<ClassMaterial> resources) {
        this.resources = resources;
    }

    public List<ClassMaterial> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<ClassMaterial> assignments) {
        this.assignments = assignments;
    }

    public List<ClassMaterial> getTasks() {
        return tasks;
    }

    public void setTasks(List<ClassMaterial> tasks) {
        this.tasks = tasks;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(int numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
