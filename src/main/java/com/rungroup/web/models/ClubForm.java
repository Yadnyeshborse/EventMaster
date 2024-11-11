package com.rungroup.web.models;

import java.time.LocalDateTime;

public class ClubForm {




    private long id;
    private String title;
    private String photoUrl;
    private String content;
    private LocalDateTime creationOn;
    private LocalDateTime updateOn;

    public ClubForm(long id, String title, String photoUrl, String content) {
        this.id = id;
        this.title = title;
        this.photoUrl = photoUrl;
        this.content = content;
        this.creationOn=LocalDateTime.now();
        this.updateOn=LocalDateTime.now();
    }

    public ClubForm() {
        this.creationOn=LocalDateTime.now();
        this.updateOn=LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
