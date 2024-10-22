package es.uca.cursoia.ingestors.bouca;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import es.uca.cursoia.WebNotifications;
import es.uca.cursoia.utils.ingestion.AbstractIngestionForm;
import es.uca.cursoia.utils.ingestion.IngestionEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class BoucaIngestionFormView extends AbstractIngestionForm {


    private final BoucaETL boucaETL;

    public BoucaIngestionFormView(BoucaETL boucaETL) {
        this.boucaETL = boucaETL;

        this.setHeaderTitle("Importar BOUCA");

        FormLayout form = new FormLayout();

        TextField urlField = new TextField("URL");
        urlField.setPlaceholder("URL del recurso a importar");
        urlField.setRequired(true);
        form.add(urlField);

        add(form);


        Button addButton = new Button("Ingerir contenido", e -> {
            String url = urlField.getValue();

            CompletableFuture<Void> future = boucaETL.index(url);
            // Al comenzar la ingesta, se muestra una notificación y al finalizar se muestra otra diferente
            future.thenAccept((v) -> {
                WebNotifications.showSuccessNotification("BOUCA importado correctamente..");
            });

            WebNotifications.showSuccessNotification("Importando BOUCA. Esta operación tarda unos instantes");
            urlField.setValue("");
            fireEvent(new IngestionEvent(this, false));
            this.close();
        });

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        this.getFooter().add(addButton);


    }


}
