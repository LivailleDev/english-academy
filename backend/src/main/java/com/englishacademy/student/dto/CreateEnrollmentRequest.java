package com.englishacademy.student.dto;

import jakarta.validation.constraints.NotNull;

public record CreateEnrollmentRequest(
        @NotNull Long studentId,
        @NotNull Long courseId
) {
}
