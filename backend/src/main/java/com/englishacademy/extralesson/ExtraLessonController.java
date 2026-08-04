package com.englishacademy.extralesson;

import com.englishacademy.extralesson.dto.CreateExtraLessonRequest;
import com.englishacademy.extralesson.dto.ExtraLessonResponse;
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
@RequestMapping("/api/extra-lessons")
public class ExtraLessonController {

    private final ExtraLessonService extraLessonService;

    public ExtraLessonController(ExtraLessonService extraLessonService) {
        this.extraLessonService = extraLessonService;
    }

    @GetMapping
    public List<ExtraLessonResponse> findAll() {
        return extraLessonService.findAll();
    }

    @GetMapping("/{id}")
    public ExtraLessonResponse findById(@PathVariable Long id) {
        return extraLessonService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ExtraLessonResponse> create(@Valid @RequestBody CreateExtraLessonRequest request) {
        ExtraLessonResponse created = extraLessonService.create(request);
        return ResponseEntity.created(URI.create("/api/extra-lessons/" + created.id())).body(created);
    }
}
