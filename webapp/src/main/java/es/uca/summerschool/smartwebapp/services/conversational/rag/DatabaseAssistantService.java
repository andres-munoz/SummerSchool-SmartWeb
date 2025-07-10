package es.uca.summerschool.smartwebapp.services.conversational.rag;

import dev.langchain4j.experimental.rag.content.retriever.sql.SqlDatabaseContentRetriever;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import es.uca.summerschool.smartwebapp.services.conversational.Conversational;
import es.uca.summerschool.smartwebapp.services.conversational.Searchable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseAssistantService implements Conversational, Searchable {

    protected static final String WELCOME_MESSAGE = """
            Hi %s, I am a virtual assistant able to search data from the database. What can I help you?
            """;

    protected static final String SYSTEM_PROMPT_TEMPLATE = """
            You are an AI-powered assistant to query the book database. 
            """;

    final DataSource dataSource;
    private final ChatModel chatModel;
    private final Conversational service;
    private ContentRetriever contentRetriever;

    public DatabaseAssistantService(StreamingChatModel streamingChatModel, ChatModel chatModel, DataSource dataSource) {
        this.chatModel = chatModel;
        this.dataSource = dataSource;

        contentRetriever = SqlDatabaseContentRetriever.builder()
                .dataSource(dataSource)
                .chatModel(chatModel)
                .build();

        service = AiServices.builder(Conversational.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> SYSTEM_PROMPT_TEMPLATE)
                .contentRetriever(contentRetriever) // it should have access to our documents
                .build();
    }


    public List<List<String>> search(String naturalLanguageQuery) {
        // Issue a query to the database
        Query query = Query.from(naturalLanguageQuery);
        System.out.println("Executing query: " + query.text());
        List<Content> content = contentRetriever.retrieve(query);
        System.out.println("Content retrieved: " + content.size());

        // Process the content to extract the text, // assuming the first content is the one we want
        String text = content.get(0).textSegment().text();
        text = text.substring(text.indexOf(":") + 2);

        System.out.println("Text extracted: " + text);

        // Each line is a row and each column is separated by commas
        List<List<String>> result = new ArrayList<>();
        String[] lines = text.split("\n");
        for (String line : lines) {
            List<String> row = new ArrayList<>();
            String[] columns = line.split(",");
            for (String column : columns) {
                row.add(column);
            }
            result.add(row);
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