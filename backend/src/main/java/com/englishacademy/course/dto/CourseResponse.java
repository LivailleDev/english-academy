package com.englishacademy.course.dto;

import com.englishacademy.course.Course;
import com.englishacademy.course.CourseLevel;
import java.time.Instant;
import java.util.List;

public record CourseResponse(
        Long id,
        String title,
        String description,
        CourseLevel level,
        int durationHours,
        Instant createdAt,
        List<LessonResponse> lessons
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getDurationHours(),
                course.getCreatedAt(),
                course.getLessons().stream().map(LessonResponse::from).toList()
        );
    }

    /** Summary view without lessons, used for list endpoints. */
    public static CourseResponse summary(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getDurationHours(),
                course.getCreatedAt(),
                List.of()
        );
    }
}
