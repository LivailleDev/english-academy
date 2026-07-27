package com.englishacademy.student;

import com.englishacademy.course.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test covering the enrollment flow (student + course -> enrollment,
 * plus the duplicate-enrollment conflict) against a real MySQL instance.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class EnrollmentControllerIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void resetState() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void enrollsStudentInCourseAndRejectsDuplicateEnrollment() {
        Long studentId = createStudent("Ana Souza", "ana.souza@example.com");
        Long courseId = createCourse("Business English");

        Map<String, Object> enrollPayload = Map.of("studentId", studentId, "courseId", courseId);

        ResponseEntity<Map<String, Object>> firstEnroll = restTemplate.exchange(
                "/api/enrollments", HttpMethod.POST, new HttpEntity<>(enrollPayload),
                new ParameterizedTypeReference<>() {
                });
        assertThat(firstEnroll.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(firstEnroll.getBody().get("status")).isEqualTo("IN_PROGRESS");

        ResponseEntity<Map<String, Object>> duplicateEnroll = restTemplate.exchange(
                "/api/enrollments", HttpMethod.POST, new HttpEntity<>(enrollPayload),
                new ParameterizedTypeReference<>() {
                });
        assertThat(duplicateEnroll.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void rejectsDuplicateStudentEmail() {
        createStudent("Ana Souza", "ana.souza@example.com");

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/students", HttpMethod.POST,
                new HttpEntity<>(Map.of("name", "Outra Ana", "email", "ana.souza@example.com")),
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    private Long createStudent(String name, String email) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/students", HttpMethod.POST, new HttpEntity<>(Map.of("name", name, "email", email)),
                new ParameterizedTypeReference<>() {
                });
        return ((Number) response.getBody().get("id")).longValue();
    }

    private Long createCourse(String title) {
        Map<String, Object> payload = Map.of(
                "title", title,
                "description", "desc",
                "level", "B1",
                "durationHours", 10
        );
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/courses", HttpMethod.POST, new HttpEntity<>(payload),
                new ParameterizedTypeReference<>() {
                });
        return ((Number) response.getBody().get("id")).longValue();
    }
}
