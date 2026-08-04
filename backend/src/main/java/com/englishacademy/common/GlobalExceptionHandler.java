package com.englishacademy.common;

import com.englishacademy.course.CourseNotFoundException;
import com.englishacademy.extralesson.ExtraLessonNotFoundException;
import com.englishacademy.student.AlreadyEnrolledException;
import com.englishacademy.student.DuplicateEmailException;
import com.englishacademy.student.StudentNotFoundException;
import com.englishacademy.studymodule.StudyModuleNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Translates domain exceptions into RFC 7807 problem+json responses
 * (Spring's built-in ProblemDetail, rather than a hand-rolled error body).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CourseNotFoundException.class,
            StudentNotFoundException.class,
            ExtraLessonNotFoundException.class,
            StudyModuleNotFoundException.class
    })
    public ProblemDetail handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return problem(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), request);
    }

    @ExceptionHandler({DuplicateEmailException.class, AlreadyEnrolledException.class})
    public ProblemDetail handleConflict(RuntimeException ex, HttpServletRequest request) {
        return problem(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        return problem(HttpStatus.BAD_REQUEST, "Validation failed", detail, request);
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return problemDetail;
    }
}
