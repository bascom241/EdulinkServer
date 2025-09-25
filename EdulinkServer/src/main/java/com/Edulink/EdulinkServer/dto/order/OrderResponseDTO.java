package com.Edulink.EdulinkServer.dto.order;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderResponseDTO {
    private Long id;
    private String date;
    private BigDecimal amount;
    private String status;
    private String studentName;
    private String classroomDescription;
    private BigDecimal classroomPrice;

    public String getClassroomDescription() {
        return classroomDescription;
    }

    public void setClassroomDescription(String classroomDescription) {
        this.classroomDescription = classroomDescription;
    }

    public BigDecimal getClassroomPrice() {
        return classroomPrice;
    }

    public void setClassroomPrice(BigDecimal classroomPrice) {
        this.classroomPrice = classroomPrice;
    }

    private String classroomName;

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
