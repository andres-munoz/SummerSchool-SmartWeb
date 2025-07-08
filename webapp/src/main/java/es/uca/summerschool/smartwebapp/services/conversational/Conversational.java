package es.uca.summerschool.smartwebapp.services.conversational;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface Conversational {
    public Flux<String> chat(@MemoryId String chatSessionId, @UserMessage String userMessage);

    public String getWelcomeMessage(String username);

}
