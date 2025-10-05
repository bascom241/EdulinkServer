package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Classroom , Long> {
    List<Classroom> findByExpiresAtBefore(LocalDateTime localDateTime);


    long countByOwner_Email(String email);

    List<Classroom> findByOwner_Email(String email);


    @Query("SELECT c FROM Classroom c JOIN c.students s WHERE s.email = :email")
    List<Classroom> findAllByStudentEmail(@Param("email") String email);


}
