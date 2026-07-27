package com.englishacademy.student;

import com.englishacademy.course.Course;
import com.englishacademy.course.CourseLevel;
import com.englishacademy.course.CourseNotFoundException;
import com.englishacademy.course.CourseRepository;
import com.englishacademy.student.dto.CreateEnrollmentRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;

    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        enrollmentService = new EnrollmentService(enrollmentRepository, studentRepository, courseRepository);
    }

    @Test
    void enrollThrowsWhenStudentDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enroll(new CreateEnrollmentRequest(1L, 2L)))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void enrollThrowsWhenCourseDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student("Ana", "ana@example.com")));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enroll(new CreateEnrollmentRequest(1L, 2L)))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void enrollThrowsWhenAlreadyEnrolled() {
        Student student = new Student("Ana", "ana@example.com");
        Course course = new Course("Business English", "desc", CourseLevel.B1, 20);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enroll(new CreateEnrollmentRequest(1L, 2L)))
                .isInstanceOf(AlreadyEnrolledException.class);
    }

    @Test
    void enrollSavesNewEnrollmentWithInProgressStatus() {
        Student student = new Student("Ana", "ana@example.com");
        Course course = new Course("Business English", "desc", CourseLevel.B1, 20);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(any(), any())).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = enrollmentService.enroll(new CreateEnrollmentRequest(1L, 2L));

        assertThat(response.status()).isEqualTo(EnrollmentStatus.IN_PROGRESS);
        assertThat(response.courseTitle()).isEqualTo("Business English");
    }
}
