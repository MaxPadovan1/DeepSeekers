package com.example.teach.TempBackendTesting;
import com.example.teach.model.Student;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;

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

        students.add(new Student("S00000001", "student1pw", "Bob", "Baker"));
        students.add(new Student("S00000002", "student2pw", "Dave", "Smith"));

        teachers.add(new Teacher("T00000001", "teacher1pwd", "Lucy", "Davis"));
        teachers.add(new Teacher("T00000002", "teacher2pwd", "Sam", "Balls"));
    }

    public List<? extends User> getStudents()
    {
        return students;
    }

    public List<? extends User> getTeachers()
    {
        return teachers;
    }
}