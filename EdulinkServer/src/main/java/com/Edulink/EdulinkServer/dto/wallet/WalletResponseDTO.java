package com.Edulink.EdulinkServer.dto.wallet;

import com.Edulink.EdulinkServer.dto.order.OrderResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class WalletResponseDTO {

    private Long walletId;
    private Long userId;

    private BigDecimal balance;
    private BigDecimal totalEarnings;
    private BigDecimal withdrawn;
    private List<OrderResponseDTO> transactions;

    public List<OrderResponseDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<OrderResponseDTO> transactions) {
        this.transactions = transactions;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
