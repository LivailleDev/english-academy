package com.englishacademy.student.dto;

import com.englishacademy.student.Student;
import java.time.Instant;

public record StudentResponse(Long id, String name, String email, Instant createdAt) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getEmail(), student.getCreatedAt());
    }
}
