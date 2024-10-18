package org.do_an.quiz_java.dto;

import org.do_an.quiz_java.model.Question;

import java.util.List;

public class UpdateQuizDTO {
    Integer id;
    String title;
    String description;
    Integer categoryId;
    List<Question> questions;
    Boolean isPublished;

}
