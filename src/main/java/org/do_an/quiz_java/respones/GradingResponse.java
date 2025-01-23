package org.do_an.quiz_java.respones;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GradingResponse {
    private float score;
    private String feedback;

    public static GradingResponse parseGradingResponse(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonResponse, GradingResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
