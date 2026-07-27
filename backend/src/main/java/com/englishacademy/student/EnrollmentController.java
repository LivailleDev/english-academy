package com.englishacademy.student;

import com.englishacademy.student.dto.CreateEnrollmentRequest;
import com.englishacademy.student.dto.EnrollmentResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/students/{studentId}/enrollments")
    public List<EnrollmentResponse> findByStudent(@PathVariable Long studentId) {
        return enrollmentService.findByStudent(studentId);
    }

    @PostMapping("/enrollments")
    public ResponseEntity<EnrollmentResponse> enroll(@Valid @RequestBody CreateEnrollmentRequest request) {
        return ResponseEntity.status(201).body(enrollmentService.enroll(request));
    }
}
