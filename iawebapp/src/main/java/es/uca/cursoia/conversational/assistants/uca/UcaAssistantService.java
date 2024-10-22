package es.uca.cursoia.conversational.assistants.uca;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import es.uca.cursoia.conversational.assistants.AssistantService;
import es.uca.cursoia.ingestors.bouca.BoucaETL;
import org.springframework.stereotype.Service;

@Service
public class UcaAssistantService extends AssistantService {

    private final UcaAssistant ucaAssistant;
    private StreamingChatLanguageModel streamingChatLanguageModel;


    public UcaAssistantService(StreamingChatLanguageModel streamingChatLanguageModel, BoucaETL boucaETL) {
        this.streamingChatLanguageModel = streamingChatLanguageModel;

        ucaAssistant = AiServices.builder(UcaAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> createSystemMessage(UcaAssistant.SPECIFIC_PROMPT_TEMPLATE, (String) chatSessionId))
                .build();
    }


    public TokenStream chat(String chatSessionId, String userMessage) {
        return ucaAssistant.chat(chatSessionId, userMessage);
    }


}