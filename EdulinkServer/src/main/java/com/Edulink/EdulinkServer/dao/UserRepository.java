package com.Edulink.EdulinkServer.dao;

import com.Edulink.EdulinkServer.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByToken(String token);

    // Find user by subaccount code
    Optional<User> findBySubAccountCode(String subAccountCode);

    // Update subaccount code
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.subAccountCode = :subAccountCode WHERE u.id = :userId")
    int updateSubAccountCode(@Param("userId") Long userId, @Param("subAccountCode") String subAccountCode);

}
