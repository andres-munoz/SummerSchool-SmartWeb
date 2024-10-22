package es.uca.cursoia.utils.ingestion;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ingestas")
public class Ingestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime lastUpdate;
    private String description;
    private String sourceId;
    private String type;
    private int numContents;

    public Ingestion() {
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumContents() {
        return numContents;
    }

    public void setNumContents(int numContents) {
        this.numContents = numContents;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }


    public Long getId() {
        return id;
    }
}
