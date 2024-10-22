package es.uca.cursoia.utils.ingestion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IngestionRepository extends JpaRepository<Ingestion, Long> {
}
