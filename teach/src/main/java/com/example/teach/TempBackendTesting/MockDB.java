package com.example.teach.TempBackendTesting;
import com.example.teach.model.Student;
import com.example.teach.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class MockDB
{
    public List<Student> students;
    public List<Teacher> teachers;

    public MockDB()
    {
        students = new ArrayList<>();
        teachers = new ArrayList<>();

        students.add(new Student("S001", "password", "Bob", "Baker"));
        students.add(new Student("S002", "leg", "Dave", "Smith"));

        teachers.add(new Teacher("T001", "penis", "Lucy", "Davis"));
        teachers.add(new Teacher("T002", "balls", "Sam", "Balls"));
    }
}