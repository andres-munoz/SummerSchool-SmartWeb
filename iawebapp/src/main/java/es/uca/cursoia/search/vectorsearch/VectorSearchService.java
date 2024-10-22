package es.uca.cursoia.search.vectorsearch;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import es.uca.cursoia.ingestors.bouca.BoucaETL;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorSearchService {

    private final BoucaETL boucaETL;

    public VectorSearchService(BoucaETL boucaETL) {
        this.boucaETL = boucaETL;


    }

    public List<EmbeddingMatch<TextSegment>> executeQuery(String text) {

        Response<Embedding> embedding = boucaETL.getEmbeddingModel().embed(text);

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(embedding.content())
                .maxResults(10)
                .minScore(0.1)
                .build();

        EmbeddingSearchResult<TextSegment> result = boucaETL.getEmbeddingStore().search(request);
        return result.matches();


    }
}
