package com.Edulink.EdulinkServer.dto.notification;

import com.Edulink.EdulinkServer.model.Notification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationDTO {
  private Long id;
  private String type;
  private String content;
  private LocalDateTime timestamp;
  private Long teacherId;
  private String classroomName;
  private Boolean isRead = false;

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public NotificationDTO() {
    }

    public NotificationDTO(Long id , LocalDateTime timestamp, String content, String type, Long teacherId , String classroomName, Boolean isRead) {
        this.id = id;
        this.timestamp = timestamp;
        this.content = content;
        this.type = type;
        this.teacherId = teacherId;
        this.classroomName = classroomName;
        this.isRead = isRead;
    }

    public NotificationDTO(Notification notification) {
        if(notification != null){
            this.type = notification.getType();
            this.content = notification.getContent();
            this.timestamp = notification.getTimestamp();
            this.isRead = notification.getRead();
            this.id = notification.getId();
            if(notification.getTeacher() != null){
                this.teacherId = notification.getTeacher().getUserId();
            }
            if(notification.getClassroom() != null){
                this.classroomName = notification.getClassroom().getClassName();
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
