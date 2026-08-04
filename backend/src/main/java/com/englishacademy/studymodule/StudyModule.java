package com.englishacademy.studymodule;

import com.englishacademy.course.CourseLevel;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Unlike Course/Lesson (a real child entity with its own identity), a module's
 * topics are simple value strings — modeled with @ElementCollection rather
 * than a one-to-many relationship, since a topic has no identity of its own.
 */
@Entity
@Table(name = "study_module")
public class StudyModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CourseLevel level;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ElementCollection
    @CollectionTable(name = "study_module_topic", joinColumns = @JoinColumn(name = "study_module_id"))
    @OrderColumn(name = "topic_order")
    @Column(name = "topic", nullable = false, length = 200)
    private List<String> topics = new ArrayList<>();

    protected StudyModule() {
    }

    public StudyModule(String title, String description, CourseLevel level, List<String> topics) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.topics = new ArrayList<>(topics);
    }

    @jakarta.persistence.PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<String> getTopics() {
        return topics;
    }
}
