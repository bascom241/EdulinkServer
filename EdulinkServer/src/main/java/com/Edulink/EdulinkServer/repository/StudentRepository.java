package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository< StudentInfo, Long> {
}
