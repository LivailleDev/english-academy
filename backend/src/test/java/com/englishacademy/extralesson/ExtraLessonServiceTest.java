package com.englishacademy.extralesson;

import com.englishacademy.extralesson.dto.CreateExtraLessonRequest;
import java.util.List;
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
class ExtraLessonServiceTest {

    @Mock
    private ExtraLessonRepository extraLessonRepository;

    private ExtraLessonService extraLessonService;

    @BeforeEach
    void setUp() {
        extraLessonService = new ExtraLessonService(extraLessonRepository);
    }

    @Test
    void createSavesAndReturnsLesson() {
        CreateExtraLessonRequest request = new CreateExtraLessonRequest(
                "Phrasal Verbs 101", "Common phrasal verbs", "...", LessonCategory.VOCABULARY, 15
        );
        when(extraLessonRepository.save(any(ExtraLesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = extraLessonService.create(request);

        assertThat(response.title()).isEqualTo("Phrasal Verbs 101");
        assertThat(response.category()).isEqualTo(LessonCategory.VOCABULARY);
        assertThat(response.durationMinutes()).isEqualTo(15);
    }

    @Test
    void findByIdThrowsWhenLessonDoesNotExist() {
        when(extraLessonRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> extraLessonService.findById(42L))
                .isInstanceOf(ExtraLessonNotFoundException.class);
    }

    @Test
    void findAllMapsEveryLesson() {
        ExtraLesson lesson = new ExtraLesson("Idioms", "desc", "content", LessonCategory.IDIOMS, 10);
        when(extraLessonRepository.findAll()).thenReturn(List.of(lesson));

        assertThat(extraLessonService.findAll()).hasSize(1);
    }
}
