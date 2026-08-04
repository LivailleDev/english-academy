package com.englishacademy.extralesson.dto;

import com.englishacademy.extralesson.LessonCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateExtraLessonRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 500) String description,
        @NotBlank @Size(max = 4000) String content,
        @NotNull LessonCategory category,
        @Min(1) int durationMinutes
) {
}
