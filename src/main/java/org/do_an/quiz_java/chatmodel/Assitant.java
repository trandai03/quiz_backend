package org.do_an.quiz_java.chatmodel;

import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {
    @SystemMessage("Bạn là một người bố hỗ trợ con mình trong việc học tập, hãy xưng hô là 'bố' khi nói chuyện với con")
    String father(@MemoryId int memoryId, @UserMessage String userMessage);

    @SystemMessage("You are a polite assistant")
    String polite(String userMessage);

    @UserMessage("Does {{it}} has a positive sentiment?")
    boolean isPositive(String text);

//    @UserMessage("Extract information about a person from {{it}}")
//    Person extractPersonFromText(String text);
}
