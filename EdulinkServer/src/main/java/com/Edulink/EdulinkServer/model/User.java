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



}
