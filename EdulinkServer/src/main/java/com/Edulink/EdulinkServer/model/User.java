package com.Edulink.EdulinkServer.model;

import com.Edulink.EdulinkServer.enums.TeachingLevel;
import jakarta.persistence.*;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // Identity of the user to proceed with sign up algorithm
    private boolean student;
    private boolean teacher;

    // User Security Details
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String confirmPassword;
    private String role;

    // User Personal details
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // User Professional details
    private String[] teachingSubjects;
    private TeachingLevel teachingLevel;
    private String shortBio;
    private int yearsOfExperience;

    ///  Certificate Image Collection Data
    private String certificateUrl;
    private String certificateImageType;
    private String certificateImageName;

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getCertificateImageType() {
        return certificateImageType;
    }

    public void setCertificateImageType(String certificateImageType) {
        this.certificateImageType = certificateImageType;
    }

    public String getCertificateImageName() {
        return certificateImageName;
    }

    public void setCertificateImageName(String certificateImageName) {
        this.certificateImageName = certificateImageName;
    }

    public String getGovernmentIdUrl() {
        return governmentIdUrl;
    }

    public void setGovernmentIdUrl(String governmentIdUrl) {
        this.governmentIdUrl = governmentIdUrl;
    }

    public String getGovernmentIdImageName() {
        return governmentIdImageName;
    }

    public void setGovernmentIdImageName(String governmentIdImageName) {
        this.governmentIdImageName = governmentIdImageName;
    }

    public String getGovernmentIdImageType() {
        return governmentIdImageType;
    }

    public void setGovernmentIdImageType(String governmentIdImageType) {
        this.governmentIdImageType = governmentIdImageType;
    }

    /// Government Id Image Url
    private String governmentIdUrl;
    private String governmentIdImageName;
    private String governmentIdImageType;

    private String socialLink;
    private String bankAccount;
    private String bankName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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


    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
}
