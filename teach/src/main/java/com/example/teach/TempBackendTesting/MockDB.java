package com.example.teach.TempBackendTesting;
import com.example.teach.model.Student;
import java.util.ArrayList;
import java.util.List;

public class MockDB
{
    public List<Student> students;

    public MockDB()
    {
        students = new ArrayList<>();
        students.add(new Student("S001", "password", "Bob", "Baker"));
        students.add(new Student("S002", "password", "Dave", "Smith"));
    }
}