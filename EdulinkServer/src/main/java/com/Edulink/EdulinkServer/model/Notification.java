package com.Edulink.EdulinkServer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The message content
    private String content;

    // When the notification was created
    private LocalDateTime timestamp;

    // The teacher associated with this notification
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    // The classroom the notification belongs to
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    // Type of notification (e.g., TEACHER_MESSAGE, STUDENT_JOINED)
    private String type;

    private Boolean isRead = false;

    // ---------------- Getters and Setters ----------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) { this.teacher = teacher; }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Classroom getClassroom() { return classroom; }
    public void setClassroom(Classroom classroom) { this.classroom = classroom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
