package com.Edulink.EdulinkServer.dto.user;

import com.Edulink.EdulinkServer.enums.TeachingLevel;

public class UserRequestDTO{
    // Identity
    private boolean student;
    private boolean teacher;

    // Security
    private String email;
    private String password;
    private String confirmPassword;
    private String role;

    // Personal
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // Professional
    private String[] teachingSubjects;
    private TeachingLevel teachingLevel;
    private String shortBio;
    private int yearsOfExperience;
    private String certificate;
    private String governmentId;
    private String socialLink;
    private String bankAccount;
    private String bankName;

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String[] getTeachingSubjects() {
        return teachingSubjects;
    }

    public void setTeachingSubjects(String[] teachingSubjects) {
        this.teachingSubjects = teachingSubjects;
    }

    public TeachingLevel getTeachingLevel() {
        return teachingLevel;
    }

    public void setTeachingLevel(TeachingLevel teachingLevel) {
        this.teachingLevel = teachingLevel;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governmentId) {
        this.governmentId = governmentId;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
