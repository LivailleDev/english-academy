package com.englishacademy.studymodule.dto;

import com.englishacademy.course.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateStudyModuleRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 500) String description,
        @NotNull CourseLevel level,
        @NotEmpty List<@NotBlank String> topics
) {
}
