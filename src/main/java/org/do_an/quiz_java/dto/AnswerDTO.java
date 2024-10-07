package org.do_an.quiz_java.dto;

import lombok.*;

@Value
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AnswerDTO {
    Integer question_id;
    Integer is_selected;


}
