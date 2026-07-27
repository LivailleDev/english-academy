package com.englishacademy.student;

import com.englishacademy.course.Course;
import com.englishacademy.course.CourseNotFoundException;
import com.englishacademy.course.CourseRepository;
import com.englishacademy.student.dto.CreateEnrollmentRequest;
import com.englishacademy.student.dto.EnrollmentResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<EnrollmentResponse> findByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        return enrollmentRepository.findByStudentId(studentId).stream().map(EnrollmentResponse::from).toList();
    }

    @Transactional
    public EnrollmentResponse enroll(CreateEnrollmentRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new StudentNotFoundException(request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new CourseNotFoundException(request.courseId()));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new AlreadyEnrolledException(student.getId(), course.getId());
        }

        Enrollment enrollment = new Enrollment(student, course);
        return EnrollmentResponse.from(enrollmentRepository.save(enrollment));
    }
}
