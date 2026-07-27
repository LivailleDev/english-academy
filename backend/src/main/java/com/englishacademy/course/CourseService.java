package com.englishacademy.course;

import com.englishacademy.course.dto.CourseResponse;
import com.englishacademy.course.dto.CreateCourseRequest;
import com.englishacademy.course.dto.CreateLessonRequest;
import com.englishacademy.course.dto.LessonResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseResponse> findAll() {
        return courseRepository.findAll().stream().map(CourseResponse::summary).toList();
    }

    public CourseResponse findById(Long id) {
        return CourseResponse.from(getOrThrow(id));
    }

    @Transactional
    public CourseResponse create(CreateCourseRequest request) {
        Course course = new Course(request.title(), request.description(), request.level(), request.durationHours());
        return CourseResponse.from(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse update(Long id, CreateCourseRequest request) {
        Course course = getOrThrow(id);
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setLevel(request.level());
        course.setDurationHours(request.durationHours());
        return CourseResponse.from(course);
    }

    @Transactional
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }
        courseRepository.deleteById(id);
    }

    @Transactional
    public LessonResponse addLesson(Long courseId, CreateLessonRequest request) {
        Course course = getOrThrow(courseId);
        Lesson lesson = new Lesson(request.title(), request.content(), request.orderIndex());
        course.addLesson(lesson);
        return LessonResponse.from(lesson);
    }

    private Course getOrThrow(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }
}
