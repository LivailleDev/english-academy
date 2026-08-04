package com.englishacademy.extralesson;

import com.englishacademy.extralesson.dto.CreateExtraLessonRequest;
import com.englishacademy.extralesson.dto.ExtraLessonResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ExtraLessonService {

    private final ExtraLessonRepository extraLessonRepository;

    public ExtraLessonService(ExtraLessonRepository extraLessonRepository) {
        this.extraLessonRepository = extraLessonRepository;
    }

    public List<ExtraLessonResponse> findAll() {
        return extraLessonRepository.findAll().stream().map(ExtraLessonResponse::from).toList();
    }

    public ExtraLessonResponse findById(Long id) {
        return ExtraLessonResponse.from(getOrThrow(id));
    }

    @Transactional
    public ExtraLessonResponse create(CreateExtraLessonRequest request) {
        ExtraLesson lesson = new ExtraLesson(
                request.title(), request.description(), request.content(), request.category(), request.durationMinutes()
        );
        return ExtraLessonResponse.from(extraLessonRepository.save(lesson));
    }

    private ExtraLesson getOrThrow(Long id) {
        return extraLessonRepository.findById(id).orElseThrow(() -> new ExtraLessonNotFoundException(id));
    }
}
