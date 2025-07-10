package es.uca.summerschool.smartwebapp.services.conversational.rag;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.web.search.WebSearchOrganicResult;
import dev.langchain4j.web.search.WebSearchResults;
import dev.langchain4j.web.search.google.customsearch.GoogleCustomWebSearchEngine;
import es.uca.summerschool.smartwebapp.services.conversational.Conversational;
import es.uca.summerschool.smartwebapp.services.conversational.Searchable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class WebAssistantService implements Conversational, Searchable {

    protected static final String WELCOME_MESSAGE = """
            Hi %s, I am a virtual assistant expert on the UCA Vice-Rectorate of Internationalization. What can I help you?
            """;

    protected static final String SYSTEM_PROMPT_TEMPLATE = """
            You are an AI-powered assistant created by the University of Cádiz (UCA) to assist students in all matters related to information existing on the webpage of the Vice-Rectorate of Internationalization.
            """;

    private final StreamingChatModel streamingChatModel;

    private final Conversational service;
    private final ContentRetriever contentRetriever;
    private final GoogleCustomWebSearchEngine webSearchEngine;


    public WebAssistantService(StreamingChatModel streamingChatModel,
                               @Value("${google.custom.search.api.key}") String apiKey,
                               @Value("${google.custom.search.csi}") String csi) {
        this.streamingChatModel = streamingChatModel;

        webSearchEngine = GoogleCustomWebSearchEngine.builder()
                .apiKey(apiKey)
                .csi(csi)
                .logRequests(true)
                .logResponses(true)
                .build();


        contentRetriever = WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(5)
                .build();


        service = AiServices.builder(Conversational.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> SYSTEM_PROMPT_TEMPLATE)
                .contentRetriever(contentRetriever)
                .build();


    }

    @Override
    public List<List<String>> search(String query) {

        WebSearchResults aux = webSearchEngine.search(query);

        List<List<String>> result = new java.util.ArrayList<>();
        List<String> headerRow = List.of("Title", "URL", "Metadata", "Snippet");
        result.add(headerRow);
        for (WebSearchOrganicResult item : aux.results()) {
            List<String> itemList = List.of(item.title(), item.url().toString(), item.metadata().toString(), item.snippet());
            result.add(itemList);
        }

        return result;

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