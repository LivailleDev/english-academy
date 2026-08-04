package com.englishacademy.extralesson;

public class ExtraLessonNotFoundException extends RuntimeException {

    public ExtraLessonNotFoundException(Long id) {
        super("Extra lesson not found with id " + id);
    }
}
