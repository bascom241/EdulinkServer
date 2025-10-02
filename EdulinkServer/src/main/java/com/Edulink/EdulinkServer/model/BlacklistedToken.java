package com.Edulink.EdulinkServer.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , columnDefinition = "TEXT")
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiry;

    public BlacklistedToken() {
    }

    public BlacklistedToken( String token, LocalDateTime expiry) {

        this.token = token;
        this.expiry = expiry;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }
}
