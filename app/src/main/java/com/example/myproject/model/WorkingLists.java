package com.example.myproject.model;

import java.util.Date;

public class WorkingLists {
    private String title;
    private String description;
    private Date date;
    private String status;

    public WorkingLists(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
