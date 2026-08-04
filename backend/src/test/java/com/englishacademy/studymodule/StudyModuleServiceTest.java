package com.englishacademy.studymodule;

import com.englishacademy.course.CourseLevel;
import com.englishacademy.studymodule.dto.CreateStudyModuleRequest;
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
class StudyModuleServiceTest {

    @Mock
    private StudyModuleRepository studyModuleRepository;

    private StudyModuleService studyModuleService;

    @BeforeEach
    void setUp() {
        studyModuleService = new StudyModuleService(studyModuleRepository);
    }

    @Test
    void createSavesAndReturnsModuleWithTopicsInOrder() {
        CreateStudyModuleRequest request = new CreateStudyModuleRequest(
                "Grammar Bootcamp", "desc", CourseLevel.A2, List.of("Present tense", "Past tense", "Articles")
        );
        when(studyModuleRepository.save(any(StudyModule.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = studyModuleService.create(request);

        assertThat(response.title()).isEqualTo("Grammar Bootcamp");
        assertThat(response.topics()).containsExactly("Present tense", "Past tense", "Articles");
    }

    @Test
    void findByIdThrowsWhenModuleDoesNotExist() {
        when(studyModuleRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyModuleService.findById(42L))
                .isInstanceOf(StudyModuleNotFoundException.class);
    }

    @Test
    void findAllMapsEveryModule() {
        StudyModule module = new StudyModule("Module", "desc", CourseLevel.B1, List.of("Topic A"));
        when(studyModuleRepository.findAll()).thenReturn(List.of(module));

        assertThat(studyModuleService.findAll()).hasSize(1);
    }
}
