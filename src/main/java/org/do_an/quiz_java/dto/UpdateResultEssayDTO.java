package org.do_an.quiz_java.dto;

import lombok.*;

import java.util.List;

@Value
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateResultEssayDTO {
    private Integer id;
    private Float totalScore;
    List<UpdateResultEssayQuestionDTO> questions;

}
