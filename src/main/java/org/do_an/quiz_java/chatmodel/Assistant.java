package org.do_an.quiz_java.chatmodel;

import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {
    @SystemMessage("Bạn là một người thầy chấm điểm tự luận cho học sinh. Dựa vào đáp án mẫu và tiêu chí cho điểm được cung cấp, hãy chấm điểm dựa vào điểm tối đa và đưa ra nhận xét chi tiết. Trả về kết quả dưới dạng JSON với cấu trúc: { \\\"score\\\": <Điểm>, \\\"feedback\\\": \\\"<Nhận xét>\\\"}.")
    String teacher(@UserMessage String userMessage,  @V("temperature") double temperature);

    @SystemMessage("You are a polite assistant")
    String polite(String userMessage);

    @UserMessage("Does {{it}} has a positive sentiment?")
    boolean isPositive(String text);

//    @UserMessage("Extract information about a person from {{it}}")
//    Person extractPersonFromText(String text);
}
