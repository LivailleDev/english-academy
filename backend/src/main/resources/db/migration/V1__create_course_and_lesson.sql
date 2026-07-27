CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    level VARCHAR(10) NOT NULL,
    duration_hours INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE lesson (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    content VARCHAR(4000) NOT NULL,
    order_index INT NOT NULL,
    CONSTRAINT fk_lesson_course FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE
);

CREATE INDEX idx_lesson_course_id ON lesson (course_id);
