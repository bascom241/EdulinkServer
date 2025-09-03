package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository< StudentInfo, Long> {
    List<StudentInfo> findByEmail(String email);
}
