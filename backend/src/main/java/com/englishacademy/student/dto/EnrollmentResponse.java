package com.englishacademy.student.dto;

import com.englishacademy.student.Enrollment;
import com.englishacademy.student.EnrollmentStatus;
import java.time.Instant;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        Long courseId,
        String courseTitle,
        EnrollmentStatus status,
        Instant enrolledAt
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt()
        );
    }
}
