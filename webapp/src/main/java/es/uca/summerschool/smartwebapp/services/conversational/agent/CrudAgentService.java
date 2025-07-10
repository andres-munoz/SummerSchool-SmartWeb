package es.uca.summerschool.smartwebapp.services.conversational.agent;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import es.uca.summerschool.smartwebapp.services.conversational.Conversational;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CrudAgentService implements Conversational {

    protected static final String WELCOME_MESSAGE = """
            Hi %s, I am a virtual assistant to help user to manage books. What can I help you?
            """;

    protected static final String SYSTEM_PROMPT_TEMPLATE = """
            You are an AI-powered assistant to manage books. You can create, read, update and delete books.
            You should ask user for all the information needed to perform the operations.
            """;
    private final StreamingChatModel streamingChatModel;
    private final Conversational service;


    public CrudAgentService(StreamingChatModel streamingChatModel, BookManagementTools bookManagementTools) {
        this.streamingChatModel = streamingChatModel;

        service = AiServices.builder(Conversational.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> SYSTEM_PROMPT_TEMPLATE)
                .tools(bookManagementTools)
                .build();
    }


    @Override
    public Flux<String> chat(String chatSessionId, String userMessage) {
        return service.chat(chatSessionId, userMessage);
    }

    @Override
    public String getWelcomeMessage(String username) {
        return WELCOME_MESSAGE.formatted(username);
    }


}
