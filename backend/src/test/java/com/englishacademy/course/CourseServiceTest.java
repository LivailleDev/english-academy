package com.englishacademy.course;

import com.englishacademy.course.dto.CreateCourseRequest;
import com.englishacademy.course.dto.CreateLessonRequest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    private CourseService courseService;

    CourseServiceTest() {
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        courseService = new CourseService(courseRepository);
    }

    @Test
    void createSavesAndReturnsCourse() {
        CreateCourseRequest request = new CreateCourseRequest("Business English", "Workplace communication", CourseLevel.B1, 20);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = courseService.create(request);

        assertThat(response.title()).isEqualTo("Business English");
        assertThat(response.level()).isEqualTo(CourseLevel.B1);
        assertThat(response.durationHours()).isEqualTo(20);
    }

    @Test
    void findByIdThrowsWhenCourseDoesNotExist() {
        when(courseRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.findById(42L))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void addLessonAppendsLessonToExistingCourse() {
        Course course = new Course("Grammar Foundations", "Core grammar", CourseLevel.A2, 10);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        var response = courseService.addLesson(1L, new CreateLessonRequest("Present Simple", "...", 0));

        assertThat(response.title()).isEqualTo("Present Simple");
        assertThat(course.getLessons()).hasSize(1);
    }

    @Test
    void deleteThrowsWhenCourseDoesNotExist() {
        when(courseRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> courseService.delete(99L))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void deleteRemovesExistingCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

        courseService.delete(1L);

        verify(courseRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(1L);
    }
}
