package com.englishacademy.studymodule.dto;

import com.englishacademy.course.CourseLevel;
import com.englishacademy.studymodule.StudyModule;
import java.util.List;

public record StudyModuleResponse(
        Long id,
        String title,
        String description,
        CourseLevel level,
        List<String> topics
) {
    public static StudyModuleResponse from(StudyModule module) {
        return new StudyModuleResponse(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getLevel(),
                // copy eagerly: topics is a lazy Hibernate collection, and this DTO
                // is still serialized to JSON after the transaction (and session) closes
                List.copyOf(module.getTopics())
        );
    }
}
