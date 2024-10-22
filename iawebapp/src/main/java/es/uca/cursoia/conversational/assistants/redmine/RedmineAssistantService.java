package es.uca.cursoia.conversational.assistants.redmine;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import es.uca.cursoia.conversational.assistants.AssistantService;
import org.springframework.stereotype.Service;

@Service
public class RedmineAssistantService extends AssistantService {

    private final RedmineAssistant redmineAssistant;
    StreamingChatLanguageModel streamingChatLanguageModel;

    public RedmineAssistantService(StreamingChatLanguageModel streamingChatLanguageModel) {
        this.streamingChatLanguageModel = streamingChatLanguageModel;

        redmineAssistant = AiServices.builder(RedmineAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> createSystemMessage(RedmineAssistant.SPECIFIC_PROMPT_TEMPLATE, (String) chatSessionId))
                //  .contentRetriever(createContentRetriever())
                .tools(new RedmineAssistantTool())
                .build();

    }


    public TokenStream chat(String chatSessionId, String userMessage) {
        return redmineAssistant.chat(chatSessionId, userMessage);
    }


}
