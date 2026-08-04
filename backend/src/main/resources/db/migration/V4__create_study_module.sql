CREATE TABLE study_module (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL,
    level VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE study_module_topic (
    study_module_id BIGINT NOT NULL,
    topic VARCHAR(200) NOT NULL,
    topic_order INT NOT NULL,
    CONSTRAINT fk_study_module_topic_module FOREIGN KEY (study_module_id) REFERENCES study_module (id) ON DELETE CASCADE
);

CREATE INDEX idx_study_module_topic_module_id ON study_module_topic (study_module_id);
