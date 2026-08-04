package com.englishacademy.student;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Student not found with id " + id);
    }

    public StudentNotFoundException(String email) {
        super("Student not found with email " + email);
    }
}
