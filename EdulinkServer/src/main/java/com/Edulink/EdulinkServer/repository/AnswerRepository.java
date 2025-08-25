package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<StudentAnswer, Long> {
}
