package es.uca.summerschool.smartwebapp.services.conversational.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.*;
import es.uca.summerschool.smartwebapp.services.conversational.Conversational;
import es.uca.summerschool.smartwebapp.services.conversational.Searchable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentAssistantService implements Conversational, Searchable {

    protected static final String WELCOME_MESSAGE = """
            Hi %s, I am a virtual assistant expert on the 'Building Smart Web Apps with AI' course. What can I help you?
            """;

    protected static final String SYSTEM_PROMPT_TEMPLATE = """
                        You are an AI-powered assistant created by the University of Cádiz (UCA) to assist students of the 'Building Smart Web Apps with AI' course.
            """;

    private final Conversational service;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore embeddingStore;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public DocumentAssistantService(StreamingChatModel streamingChatModel, EmbeddingStore embeddingStore, EmbeddingModel embeddingModel) {

        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;


        indexInformation();


        service = AiServices.builder(Conversational.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> SYSTEM_PROMPT_TEMPLATE)
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();


    }

    public static PathMatcher glob(String glob) {
        return FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }


    private Path toPath(String relativePath) {
        try {
            URL fileUrl = DocumentAssistantService.class.getClassLoader().getResource(relativePath);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public void indexInformation() {

        logger.info("Ingesting documents from folder");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(toPath("documents/"));

        logger.info(">>> Embedding and storing " + documents.size() + " documents into a memory store...");
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        logger.info(">>> Documents ingested...");

    }


    @Override
    public Flux<String> chat(String chatSessionId, String userMessage) {
        return service.chat(chatSessionId, userMessage);
    }

    @Override
    public String getWelcomeMessage(String username) {
        return WELCOME_MESSAGE.formatted(username);
    }


    @Override
    public List<List<String>> search(String text) {
        Response<Embedding> embedding = embeddingModel.embed(text);


        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(embedding.content())
                .maxResults(10)
                .minScore(0.1)
                .build();

        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(request);
        List<EmbeddingMatch<TextSegment>> embeddingMatches = searchResult.matches();

        List<List<String>> result = new ArrayList<>();
        result.add(List.of("Score", "Text", "Metadata", "Embedding"));
        for (EmbeddingMatch<TextSegment> match : embeddingMatches) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(match.score()));
            row.add(match.embedded().text());
            row.add(match.embedded().metadata().toString());
            row.add(match.embedding().toString());
            result.add(row);
        }

        return result;
    }
}
