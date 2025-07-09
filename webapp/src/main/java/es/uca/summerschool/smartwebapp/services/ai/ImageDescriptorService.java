package es.uca.summerschool.smartwebapp.services.ai;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ImageDescriptorService {

    ChatModel chatModel;

    public ImageDescriptorService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }


    public String describe(byte[] image) {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("Describe this image: "),
                ImageContent.from(Base64.getEncoder().encodeToString(image), "image/png")
        );

        ChatResponse response = chatModel.chat(userMessage);

        return response.aiMessage().text();
    }
}
