package com.example.teach.model;
/**
 * Represents a study file uploaded by a teacher or associated with a subject.
 * <p>
 * Contains metadata such as the original file name, a user-defined title,
 * and the binary content of the file as a byte array.
 */
public class StudyFile {
    /** The original name of the uploaded file (e.g., "lecture1.pdf"). */
    private final String fileName;
    /** A descriptive or display title for the file (e.g., "Week 1 Notes"). */
    private final String title;
    /** The raw binary data of the file. */
    private final byte[] data;
    /**
     * Constructs a new StudyFile instance with the provided file metadata and content.
     *
     * @param fileName the original name of the file
     * @param title    a user-friendly title for the file
     * @param data     the binary contents of the file
     */
    public StudyFile(String fileName, String title, byte[] data) {
        this.fileName = fileName;
        this.title = title;
        this.data = data;
    }
    /**
     * Returns the original file name.
     *
     * @return file name
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * Returns the descriptive title of the file.
     *
     * @return file title
     */
    public String getTitle() {
        return title;
    }
    /**
     * Returns the binary contents of the file.
     *
     * @return byte array containing file data
     */
    public byte[] getData() {
        return data;
    }
}