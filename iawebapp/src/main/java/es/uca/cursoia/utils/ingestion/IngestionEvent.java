package es.uca.cursoia.utils.ingestion;

import com.vaadin.flow.component.ComponentEvent;

public class IngestionEvent
        extends ComponentEvent<AbstractIngestionForm> {
    public IngestionEvent(AbstractIngestionForm source,
                          boolean fromClient) {
        super(source, fromClient);
    }
}