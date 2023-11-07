package com.example.tttn2023.model;

import java.io.Serializable;

public class Project implements Serializable {
    private String id;
    private String name;

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    public Project(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
