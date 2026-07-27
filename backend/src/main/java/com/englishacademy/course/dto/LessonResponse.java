package com.englishacademy.course.dto;

import com.englishacademy.course.Lesson;

public record LessonResponse(
        Long id,
        String title,
        String content,
        int orderIndex
) {
    public static LessonResponse from(Lesson lesson) {
        return new LessonResponse(lesson.getId(), lesson.getTitle(), lesson.getContent(), lesson.getOrderIndex());
    }
}
