package es.uca.summerschool.smartwebapp.services.ai;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class TextGeneratorService {

    ChatModel chatModel;

    public TextGeneratorService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generate(String prompt) {
        return chatModel.chat(prompt);
    }


}
