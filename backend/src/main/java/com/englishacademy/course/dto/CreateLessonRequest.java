package com.englishacademy.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLessonRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 4000) String content,
        @Min(0) int orderIndex
) {
}
