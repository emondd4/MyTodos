package com.example.mytodos.ModelClass;

import java.io.Serializable;

public class UserNotes implements Serializable {
    private String title,desc,date,id;

    public UserNotes() {
    }

    public UserNotes(String title, String desc, String date, String id) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
