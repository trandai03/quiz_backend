package org.do_an.quiz_java.chatmodel;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import lombok.Value;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@AiService
public interface Assistant {


    String apiKey  = "${langchain4j.open-ai.streaming-chat-model.api-key}";
    @SystemMessage("Bạn là một người thầy chấm điểm tự luận cho học sinh. Dựa vào đáp án mẫu và tiêu chí cho điểm được cung cấp, hãy chấm điểm dựa vào điểm tối đa và đưa ra nhận xét chi tiết. Trả về kết quả dưới dạng JSON với cấu trúc: { \\\"score\\\": <Điểm>, \\\"feedback\\\": \\\"<Nhận xét>\\\"}.")
    String teacher(@UserMessage String userMessage,  @V("temperature") double temperature);

//    ChatLanguageModel model = OpenAiChatModel.builder()
//            .apiKey(System.getenv(apiKey))
//            .modelName(GPT_4_O_MINI)
//            .build();
}
