package com.englishacademy.student;

import com.englishacademy.student.dto.CreateStudentRequest;
import com.englishacademy.student.dto.StudentResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentResponse> findAll() {
        return studentRepository.findAll().stream().map(StudentResponse::from).toList();
    }

    public StudentResponse findById(Long id) {
        return StudentResponse.from(getOrThrow(id));
    }

    @Transactional
    public StudentResponse create(CreateStudentRequest request) {
        if (studentRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        Student student = new Student(request.name(), request.email());
        return StudentResponse.from(studentRepository.save(student));
    }

    Student getOrThrow(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }
}
