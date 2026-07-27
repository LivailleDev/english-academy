package com.englishacademy.course.dto;

import com.englishacademy.course.CourseLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(
        @NotBlank @jakarta.validation.constraints.Size(max = 150) String title,
        @NotBlank @jakarta.validation.constraints.Size(max = 1000) String description,
        @NotNull CourseLevel level,
        @Min(1) int durationHours
) {
}
