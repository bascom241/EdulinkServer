package com.Edulink.EdulinkServer.model;

import com.Edulink.EdulinkServer.enums.QuestionType;
import com.Edulink.EdulinkServer.model.StudentInfo;
import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    public StudentAnswer() {
    }

    @ManyToOne
    private Question question;

    @ManyToOne
    private StudentInfo studentInfo;

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String answer;
}
