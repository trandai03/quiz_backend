ALTER TABLE user_essay_answers DROP FOREIGN KEY user_essay_answers_ibfk_2;

ALTER TABLE user_essay_answers ADD CONSTRAINT user_essay_answers_ibfk_2
    FOREIGN KEY (question_id) REFERENCES essay_questions(id)
        ON DELETE CASCADE;
