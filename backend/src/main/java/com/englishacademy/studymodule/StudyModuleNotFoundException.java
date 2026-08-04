package com.englishacademy.studymodule;

public class StudyModuleNotFoundException extends RuntimeException {

    public StudyModuleNotFoundException(Long id) {
        super("Study module not found with id " + id);
    }
}
