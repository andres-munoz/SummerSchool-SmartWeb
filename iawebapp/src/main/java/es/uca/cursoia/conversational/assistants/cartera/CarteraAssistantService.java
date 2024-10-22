package es.uca.cursoia.conversational.assistants.cartera;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import es.uca.cursoia.conversational.assistants.AssistantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CarteraAssistantService extends AssistantService {

    private final CarteraAssistant carteraAssistant;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public CarteraAssistantService(StreamingChatLanguageModel streamingChatLanguageModel) {
        this.streamingChatLanguageModel = streamingChatLanguageModel;

        carteraAssistant = AiServices.builder(CarteraAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> createSystemMessage(CarteraAssistant.SPECIFIC_PROMPT_TEMPLATE, (String) chatSessionId))
                .contentRetriever(createContentRetriever()) // it should have access to our documents
                .build();

    }

    public static PathMatcher glob(String glob) {
        return FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }

    private Path toPath(String relativePath) {
        try {
            URL fileUrl = CarteraAssistantService.class.getClassLoader().getResource(relativePath);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    private ContentRetriever createContentRetriever() {

        logger.info("Ingesting documents from folder");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(toPath("documents/"), glob("*.pdf"));

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        logger.info(">>> Embedding and storing " + documents.size() + " documents into a memory store...");
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        logger.info(">>> Documents ingested...");

        // Lastly, let's create a content retriever from an embedding store.
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }


    public TokenStream chat(String chatSessionId, String userMessage) {
        return carteraAssistant.chat(chatSessionId, userMessage);
    }


}
