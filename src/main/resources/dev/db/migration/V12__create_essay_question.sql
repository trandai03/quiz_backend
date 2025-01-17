CREATE TABLE essay_questions (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 quiz_id INT NOT NULL, -- Khóa ngoại liên kết với quiz
                                 question_text TEXT NOT NULL,
                                 model_answer TEXT NOT NULL,
                                 scoring_criteria TEXT NOT NULL,
                                 is_auto_scored BOOLEAN NOT NULL DEFAULT 0, -- Xác định chấm tự động hay không
                                 max_score FLOAT NOT NULL DEFAULT 10,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     FOREIGN KEY (quiz_id) REFERENCES quizzes(id) -- Liên kết với bảng quizzes
);

CREATE TABLE user_essay_answers (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_id INT NOT NULL, -- Khóa ngoại liên kết với bảng users
                                    question_id INT NOT NULL, -- Liên kết với câu hỏi tự luận
                                    user_answer TEXT NOT NULL,
                                    score FLOAT DEFAULT NULL,
                                    feedback TEXT DEFAULT NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (user_id) REFERENCES users(id), -- Liên kết với bảng users
                                    FOREIGN KEY (question_id) REFERENCES essay_questions(id) -- Liên kết với bảng essay_questions
);
ALTER TABLE `quizzes`
    ADD COLUMN `type` TEXT DEFAULT NULL;