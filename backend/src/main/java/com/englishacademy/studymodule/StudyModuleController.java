package com.englishacademy.studymodule;

import com.englishacademy.studymodule.dto.CreateStudyModuleRequest;
import com.englishacademy.studymodule.dto.StudyModuleResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study-modules")
public class StudyModuleController {

    private final StudyModuleService studyModuleService;

    public StudyModuleController(StudyModuleService studyModuleService) {
        this.studyModuleService = studyModuleService;
    }

    @GetMapping
    public List<StudyModuleResponse> findAll() {
        return studyModuleService.findAll();
    }

    @GetMapping("/{id}")
    public StudyModuleResponse findById(@PathVariable Long id) {
        return studyModuleService.findById(id);
    }

    @PostMapping
    public ResponseEntity<StudyModuleResponse> create(@Valid @RequestBody CreateStudyModuleRequest request) {
        StudyModuleResponse created = studyModuleService.create(request);
        return ResponseEntity.created(URI.create("/api/study-modules/" + created.id())).body(created);
    }
}
