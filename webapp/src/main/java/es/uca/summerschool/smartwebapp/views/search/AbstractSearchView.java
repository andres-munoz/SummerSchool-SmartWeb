package es.uca.summerschool.smartwebapp.views.search;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.summerschool.smartwebapp.services.conversational.Searchable;

import java.util.Collections;
import java.util.List;

@AnonymousAllowed
public abstract class AbstractSearchView extends VerticalLayout {

    protected TextArea input;
    private Grid<List<String>> grid;


    public AbstractSearchView(Searchable searchService) {
        super();

        // Build the form
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("60%");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        input = new TextArea();
        input.setLabel("Query in natural language");
        formLayout.add(input);

        Button button = new Button("Ask");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formLayout.add(button);

        // Set the layout
        this.add(formLayout);

        add(new H3("Results:"));
        grid = new Grid();
        add(grid);

        // Add the button click listener
        button.addClickListener(e -> {

            // Clear previous results
            grid.removeAllColumns();
            grid.setItems(Collections.emptyList());

            // Issue the query
            List<List<String>> result = searchService.search(input.getValue());

            // Header
            List<String> headerRow = result.get(0);

            // Create columns based on the header row
            for (int i = 0; i < headerRow.size(); i++) {
                final int idx = i; // must be final or effectively final to use in lambda expressions
                grid.addColumn(row -> row.get(idx))
                        .setHeader(headerRow.get(idx))
                        .setAutoWidth(true);
            }


            // Set the items, excluding the header row
            grid.setItems(result.subList(1, result.size()));


        });


        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setSizeFull();
        this.setAlignSelf(Alignment.CENTER, formLayout);


    }


}
