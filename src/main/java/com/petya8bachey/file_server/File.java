package com.petya8bachey.file_server;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class File {
    @Id
    private String name;
    private String content;

    public File(String name) {
        this.name = name;
        this.content = "";
    }

    public File(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public File() {
    }

    public String getName() {
        return name;
    }

    public synchronized String getContent() {
        return content;
    }

    public synchronized void setContent(String content) {
        this.content = content;
    }
}