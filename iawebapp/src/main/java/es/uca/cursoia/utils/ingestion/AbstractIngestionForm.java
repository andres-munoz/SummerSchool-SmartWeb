package es.uca.cursoia.utils.ingestion;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.shared.Registration;


public class AbstractIngestionForm extends Dialog {


    public AbstractIngestionForm() {

        this.setModal(true);
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(true);
        this.setWidth("50%");


        Button closeButton = new Button("Cerrar", (e) -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.getFooter().add(closeButton);

    }

    public Registration addInjectionListener(
            ComponentEventListener<IngestionEvent> listener) {
        return addListener(IngestionEvent.class, listener);
    }


}
