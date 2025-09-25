package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet , Long> {

    Optional <Wallet> findByWalletOwner_UserId(Long userId);
}
