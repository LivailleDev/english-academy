package com.englishacademy.extralesson.dto;

import com.englishacademy.extralesson.ExtraLesson;
import com.englishacademy.extralesson.LessonCategory;

public record ExtraLessonResponse(
        Long id,
        String title,
        String description,
        String content,
        LessonCategory category,
        int durationMinutes
) {
    public static ExtraLessonResponse from(ExtraLesson lesson) {
        return new ExtraLessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getContent(),
                lesson.getCategory(),
                lesson.getDurationMinutes()
        );
    }
}
