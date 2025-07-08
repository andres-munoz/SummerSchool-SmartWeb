package es.uca.summerschool.smartwebapp.views.ai;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.summerschool.smartwebapp.services.ai.TextGeneratorService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Text Generator")
@Route(value = "generators/text", layout = MainLayout.class)
@Menu(order = 3, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@AnonymousAllowed
public class TextGeneratorView extends VerticalLayout {

    private final TextGeneratorService textGeneratorService;

    public TextGeneratorView(TextGeneratorService textGeneratorService) {
        this.textGeneratorService = textGeneratorService;

        // Build the form
        FormLayout formLayout = new FormLayout();
        formLayout.setAutoResponsive(true);
        formLayout.setWidth("60%");
        formLayout.setExpandColumns(true);
        formLayout.setExpandFields(true);


        TextArea input = new TextArea();
        input.setLabel("Prompt");
        input.setPlaceholder("Write your prompt here");
        input.setWidth("2000px");
        formLayout.add(input);

        Button button = new Button("Generate");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formLayout.add(button);

        TextArea output = new TextArea();
        output.setLabel("Result");
        output.setReadOnly(true);
        formLayout.add(output);

        // Add the button click listener
        button.addClickListener(e -> {
            String text = input.getValue();
            String result = textGeneratorService.generate(text);
            output.setValue(result);
        });

        // Set the layout
        this.add(formLayout);
        this.setJustifyContentMode(JustifyContentMode.CENTER); // Vertical alignment

        this.setAlignSelf(Alignment.CENTER, formLayout);
        this.setWidthFull();
    }


}