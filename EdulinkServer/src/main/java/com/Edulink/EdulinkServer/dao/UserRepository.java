package com.Edulink.EdulinkServer.dao;

import com.Edulink.EdulinkServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
    User findByEmail(String email);

}
