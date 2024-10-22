package es.uca.cursoia.utils.ingestion;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestionService {
    private IngestionRepository repo;

    public IngestionService(IngestionRepository repo) {
        this.repo = repo;
    }

    public List<Ingestion> findAll() {
        return repo.findAll();
    }

    public void remove(Ingestion e) {
        repo.delete(e);
    }
}
