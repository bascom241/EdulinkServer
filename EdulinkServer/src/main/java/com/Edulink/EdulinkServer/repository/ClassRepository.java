package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Classroom , Long> {
    List<Classroom> findByExpiresAtBefore(LocalDateTime localDateTime);
}
