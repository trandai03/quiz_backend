package org.do_an.quiz_java.dto;

import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;

@Value
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateResultEssayQuestionDTO {
    Integer id;
    Float score;
    String feedback;
}
