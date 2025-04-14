package org.do_an.quiz_java.respones;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class GradingResponse {
    private float score;
    private String feedback;

    public static GradingResponse parseGradingResponse(String jsonResponse) {
        try {
            // Làm sạch response
            String cleanedResponse = cleanJsonResponse(jsonResponse);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(cleanedResponse, GradingResponse.class);
        } catch (Exception e) {
            log.error("Error parsing response: " + jsonResponse, e);
            return null;
        }
    }

    private static String cleanJsonResponse(String response) {
        // Loại bỏ backticks và các ký tự không mong muốn
        String cleaned = response.trim();
        // Loại bỏ backticks nếu có
        if (cleaned.startsWith("`") && cleaned.endsWith("`")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }
        // Loại bỏ json hoặc JSON nếu có
        if (cleaned.startsWith("json") || cleaned.startsWith("JSON")) {
            cleaned = cleaned.substring(cleaned.indexOf("{"));
        }
        // Đảm bảo response bắt đầu với {
        if (!cleaned.startsWith("{")) {
            cleaned = cleaned.substring(cleaned.indexOf("{"));
        }
        // Đảm bảo response kết thúc với }
        if (!cleaned.endsWith("}")) {
            cleaned = cleaned.substring(0, cleaned.lastIndexOf("}") + 1);
        }
        return cleaned;
    }
}
