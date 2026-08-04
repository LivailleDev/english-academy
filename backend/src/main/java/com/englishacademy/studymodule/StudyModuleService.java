package com.englishacademy.studymodule;

import com.englishacademy.studymodule.dto.CreateStudyModuleRequest;
import com.englishacademy.studymodule.dto.StudyModuleResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudyModuleService {

    private final StudyModuleRepository studyModuleRepository;

    public StudyModuleService(StudyModuleRepository studyModuleRepository) {
        this.studyModuleRepository = studyModuleRepository;
    }

    public List<StudyModuleResponse> findAll() {
        return studyModuleRepository.findAll().stream().map(StudyModuleResponse::from).toList();
    }

    public StudyModuleResponse findById(Long id) {
        return StudyModuleResponse.from(getOrThrow(id));
    }

    @Transactional
    public StudyModuleResponse create(CreateStudyModuleRequest request) {
        StudyModule module = new StudyModule(request.title(), request.description(), request.level(), request.topics());
        return StudyModuleResponse.from(studyModuleRepository.save(module));
    }

    private StudyModule getOrThrow(Long id) {
        return studyModuleRepository.findById(id).orElseThrow(() -> new StudyModuleNotFoundException(id));
    }
}
