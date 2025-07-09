package es.uca.summerschool.smartwebapp.services.conversational.chat;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import es.uca.summerschool.smartwebapp.services.conversational.Conversational;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MemoryChatService implements Conversational {

    protected static final String WELCOME_MESSAGE_TEMPLATE = """
            Hi %s, I am your friendly AI assistant superpowered with memory ;-)
            """;
    protected static final String SYSTEM_PROMPT_TEMPLATE = """
            "You are a funny AI that can chat and entertain users by telling jokes.\"
            """;
    private final Conversational conversationalService;

    public MemoryChatService(StreamingChatModel streamingChatModel) {

        conversationalService = AiServices.builder(Conversational.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> SYSTEM_PROMPT_TEMPLATE)
                .build();
    }

    @Override
    public String getWelcomeMessage(String username) {
        return WELCOME_MESSAGE_TEMPLATE.formatted(username);
    }

    public Flux<String> chat(String chatSessionId, String userMessage) {
        return conversationalService.chat(chatSessionId, userMessage);
    }


}
