package com.example.teach.model;

public class StudyFile {

    private final String fileName;
    private final String title;
    private final byte[] data;

    public StudyFile(String fileName, String title, byte[] data) {
        this.fileName = fileName;
        this.title = title;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getData() {
        return data;
    }
}