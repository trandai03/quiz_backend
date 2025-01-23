ALTER TABLE user_essay_answers
    ADD COLUMN result_id INT NOT NULL,
ADD CONSTRAINT fk_user_essay_answer_result FOREIGN KEY (result_id) REFERENCES results(id) ON DELETE CASCADE;
