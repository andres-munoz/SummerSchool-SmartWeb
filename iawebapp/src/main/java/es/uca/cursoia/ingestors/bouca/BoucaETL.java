package es.uca.cursoia.ingestors.bouca;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.DocumentTransformer;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenizer;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.filter.Filter;
import es.uca.cursoia.ingestors.shared.AbstractETL;
import es.uca.cursoia.utils.ingestion.Ingestion;
import es.uca.cursoia.utils.ingestion.IngestionRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Service
public class BoucaETL extends AbstractETL {

    public static final String COLLECTION_NAME = "bouca";
    private final IngestionRepository ingestionRepository;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public BoucaETL(EmbeddingStore embeddingStore, EmbeddingModel embeddingModel, IngestionRepository ingestionRepository) {
        super(embeddingModel);
        this.ingestionRepository = ingestionRepository;
    }

    @PostConstruct
    public void init() {
        this.embeddingStore = getEmbeddingStore(COLLECTION_NAME);
    }


    @Async
    public CompletableFuture<Void> evict(String url, Long ingestionID) {
        logger.info("Evicting BOUCA: " + url);

        logger.info(">>> Removing ingestion record...");
        deleteRecord(ingestionID);

        Filter filter = metadataKey("url").isEqualTo(url);
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder().filter(filter).build();
        int recordsToDelete = embeddingStore.search(request).matches().size();

        logger.info(">>> Removing " + recordsToDelete + " chunks from vector database...");
        embeddingStore.removeAll();

        logger.info(">>> BOUCA evicted...");
        return CompletableFuture.completedFuture(null);

    }

    @Async
    public CompletableFuture<Void> index(String url) {

        logger.info("Ingesting BOUCA: " + url);

        logger.info(">>> Loading document...");
        DocumentParser documentParser = new BoucaPdfDocumentParser();
        Document doc = UrlDocumentLoader.load(url, documentParser);

        logger.info(">>> Transforming document...");
        DocumentTransformer documentTransformer = new BoucaDocumentTransformer();
        List<Document> subDocs = documentTransformer.transformAll(extractDocuments(doc));

        logger.info(">>> Splitting document...");
        logger.info("    NO!");

        logger.info(">>> Embedding and storing " + subDocs.size() + " chunks...");
        ingestDocuments(subDocs);

        logger.info(">>> Saving ingestion record...");
        saveRecord(url, subDocs.size());

        logger.info(">>> BOUCA ingested...");
        return CompletableFuture.completedFuture(null);


    }

    private Ingestion saveRecord(String url, int numContents) {
        Ingestion ingestion = new Ingestion();
        ingestion.setLastUpdate(LocalDateTime.now());
        ingestion.setType("BOUCA");
        ingestion.setDescription("PDF del BOUCA");
        ingestion.setNumContents(numContents);
        ingestion.setSourceId(url);
        return ingestionRepository.save(ingestion);
    }


    private void deleteRecord(Long ingestionID) {
        ingestionRepository.deleteById(ingestionID);
    }

    private List<Document> extractDocuments(Document doc) {
        // Realmente un BOUCA es un compendio de subdocumentos
        List<Document> subDocs = new ArrayList<>();
        subDocs.add(doc);
        return subDocs;

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


}
