package com.Edulink.EdulinkServer.model;

import jakarta.persistence.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.math.BigDecimal;
@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @OneToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "userId")
    private User walletOwner;

    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal totalEarnings = BigDecimal.ZERO;
    private BigDecimal withdrawn = BigDecimal.ZERO;

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public User getWalletOwner() {
        return walletOwner;
    }

    public void setWalletOwner(User walletOwner) {
        this.walletOwner = walletOwner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(BigDecimal totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public BigDecimal getWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(BigDecimal withdrawn) {
        this.withdrawn = withdrawn;
    }
}
