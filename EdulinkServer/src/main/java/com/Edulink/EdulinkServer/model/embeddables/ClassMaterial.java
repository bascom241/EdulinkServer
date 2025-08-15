package com.Edulink.EdulinkServer.model.embeddables;

import jakarta.persistence.Embeddable;

@Embeddable
public class ClassMaterial {

    private String title;
    private String description;
    private String fileUrl;

    public ClassMaterial(){}

    public ClassMaterial(String title, String description, String fileUrl){
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
