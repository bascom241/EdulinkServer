package com.Edulink.EdulinkServer.model;

import com.Edulink.EdulinkServer.model.embeddables.ClassMaterial;

import com.Edulink.EdulinkServer.model.StudentInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @NotNull(message = "Class Name is Required")
    private String className;


    private int classroomPrice;

    public int getClassroomPrice() {
        return classroomPrice;
    }

    public void setClassroomPrice(int classroomPrice) {
        this.classroomPrice = classroomPrice;
    }


    private boolean isClassroomFull = false;

    public boolean isClassroomFull() {
        return isClassroomFull;
    }

    public void setClassroomFull(boolean classroomFull) {
        isClassroomFull = classroomFull;
    }

    @Size(max = 250 , message = "Class Description can not be more than 250 Characters")
    @Column(length = 250)
    private String classDescription;

    @NotNull(message = "Class Room Duration is Required")
    private int classDurationInDays;

    // When The Classroom is Created
    private LocalDateTime createdAt;

    // When The Classroom Expires
    private LocalDateTime expiresAt;

    //Run this method automatically before this entity is first saved to the database.
    @PrePersist
    public void Oncreate(){
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusDays(classDurationInDays);
    }

    // Specify Where the class will be taken e.g Physical, Online (Heavy Lifting By Frontend)
    @NotNull(message = "You must specify whether Online Or Physical")
    private String classDeliveryModel;

    // Specify (e.g Zoom , Whatsapp, Telegram , Videos , Address of The Physical Class )..
    @NotNull(message = "You Must Specify Class Location")
    private String classLocation;

    private String targetAudience;

    @NotNull(message = "Class Category is Necessary")
    private String classCategory;

    // To add Existing Student In You Class Room and give them access;



    // Resources
    @ElementCollection
    @CollectionTable(name = "classroom_resources" , joinColumns = @JoinColumn(name = "classroom_id"))
    private List<ClassMaterial> resources = new ArrayList<>();

    // Assignments
    @ElementCollection
    @CollectionTable(name = "classroom_assignments", joinColumns = @JoinColumn(name = "classroom_id"))
    private List<ClassMaterial> assignments = new ArrayList<>();

    // Tasks
    @ElementCollection
    @CollectionTable(name = "classroom_tasks", joinColumns = @JoinColumn(name = "classroom_id"))
    private List<ClassMaterial> tasks = new ArrayList<>();


    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @ManyToMany(mappedBy = "classrooms")
    private List<StudentInfo> students = new ArrayList<>();

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User owner;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<StudentInfo> getStudents() {
        return students;
    }

    public void setStudents(List<StudentInfo> students) {
        this.students = students;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getClassDurationInDays() {
        return classDurationInDays;
    }

    public void setClassDurationInDays(int classDurationInDays) {
        this.classDurationInDays = classDurationInDays;
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
}
