package es.uca.cursoia.ingestors.shared;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

public abstract class AbstractETL {

    public static final int MAX_BYTES_CHUNK_SIZE = 250; //300 ;
    public static final int MAX_OVERLAP = 30; //30;
    public static final int DIMENSION = 384;
    public static int MAX_RESULTS_RETRIEVED = 10;
    public static double MIN_SCORE = 0.75;
    public static int CHAT_MEMORY_MESSAGES_MAX = 15;

    private static String VECTORSTORE_URL;
    private static String VECTORSTORE_USERNAME;
    private static String VECTORSTORE_PASSWORD;
    protected final EmbeddingModel embeddingModel;
    protected EmbeddingStore<TextSegment> embeddingStore;
    Logger logger = LoggerFactory.getLogger(this.getClass());


    public AbstractETL(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;

    }

    @Value("${vectorstore.url}")
    public void setVectorStoreURL(String url) {
        VECTORSTORE_URL = url;
    }


    @Value("${vectorstore.username}")
    public void setVectorStoreUserName(String username) {
        VECTORSTORE_USERNAME = username;
    }

    @Value("${vectorstore.password}")
    public void setVectorStorePassword(String password) {
        VECTORSTORE_PASSWORD = password;
    }

    public EmbeddingStore<TextSegment> getEmbeddingStore(String collectionName) {

        return MilvusEmbeddingStore.builder()
                .uri(VECTORSTORE_URL)
                .username(VECTORSTORE_USERNAME)
                .password(VECTORSTORE_PASSWORD)
                .collectionName(collectionName)
                .dimension(DIMENSION)
                .build();
    }

    public EmbeddingStore<TextSegment> getEmbeddingStore() {
        return embeddingStore;
    }

    private String truncateStringIfNeeded(String input, int maxLength) {
        if (input.length() > maxLength) {
            return input.substring(0, maxLength);
        }
        return input;
    }


    public void ingestDocument(Document doc) {
        DocumentSplitter docSplitter = DocumentSplitters.recursive(MAX_BYTES_CHUNK_SIZE, MAX_OVERLAP, new HuggingFaceTokenizer());

        // Truncar la cadena si es necesario antes de la ingestión
        doc.metadata().toMap().forEach((key, value) -> {
            System.out.println(" META:  " + key + " -> " + value);

            if (value instanceof String) {
                String truncatedValue = truncateStringIfNeeded((String) value, 65535);
                doc.metadata().put(key, truncatedValue);

            }
        });


        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .documentSplitter(docSplitter)
                .build();
        ingestor.ingest(doc);
    }


    public void ingestDocuments(List<Document> docs) {
        DocumentSplitter docSplitter = DocumentSplitters.recursive(MAX_BYTES_CHUNK_SIZE, MAX_OVERLAP, new HuggingFaceTokenizer());

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .documentSplitter(docSplitter)
                .build();

        ingestor.ingest(docs);
    }


    public void delete(String sourceId) {

        Filter filter = metadataKey("sourceId").isEqualTo(sourceId);

        embeddingStore.removeAll(filter);
    }

    public EmbeddingModel getEmbeddingModel() {
        return embeddingModel;
    }
}
