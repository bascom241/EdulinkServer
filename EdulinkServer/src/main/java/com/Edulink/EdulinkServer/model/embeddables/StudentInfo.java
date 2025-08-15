package com.Edulink.EdulinkServer.model.embeddables;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;

@Embeddable
public class StudentInfo {

    @Email(message = "Invalid email Format")
    private String email;
    private String fullName;



    public  StudentInfo(String email , String fullName){
        this.email = email;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
