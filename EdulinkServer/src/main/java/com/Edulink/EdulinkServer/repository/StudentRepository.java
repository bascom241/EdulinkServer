package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository< StudentInfo, Long> {
    List<StudentInfo> findByEmail(String email);


    Optional <StudentInfo> findFirstByEmail(String email);
}
