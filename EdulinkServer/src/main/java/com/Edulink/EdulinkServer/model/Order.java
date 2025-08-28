package com.Edulink.EdulinkServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;   // Paystack reference
    private Integer amount;     // Total paid by student (in kobo)
    private Integer settlementAmount; // Amount classroom owner actually receives
    private String currency;    // e.g. "NGN"
    private String status;      // success, failed, pending

    private String subaccountCode; // Paystack subaccount for classroom owner

    @ManyToOne
    @JoinColumn(name = "orders")
    @JsonIgnore
    // student who paid
    private User student;



    @ManyToOne
    @JoinColumn(name = "owner_id")  // classroom owner
    private User classroomOwner;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(Integer settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubaccountCode() {
        return subaccountCode;
    }

    public void setSubaccountCode(String subaccountCode) {
        this.subaccountCode = subaccountCode;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getClassroomOwner() {
        return classroomOwner;
    }

    public void setClassroomOwner(User classroomOwner) {
        this.classroomOwner = classroomOwner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
