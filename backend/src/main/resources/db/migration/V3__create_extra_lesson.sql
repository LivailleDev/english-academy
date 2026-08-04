CREATE TABLE extra_lesson (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL,
    content VARCHAR(4000) NOT NULL,
    category VARCHAR(20) NOT NULL,
    duration_minutes INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_extra_lesson_category ON extra_lesson (category);
