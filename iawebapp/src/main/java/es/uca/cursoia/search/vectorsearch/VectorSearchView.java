package es.uca.cursoia.search.vectorsearch;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import es.uca.cursoia.MainLayout;

import java.util.List;

@PageTitle("Búsqueda semántica en base de datos vectorial de BOUCA")
@Route(value = "vectorsearch", layout = MainLayout.class)
public class VectorSearchView extends VerticalLayout {
    public VectorSearchView(VectorSearchService vectorSearchService) {
        super();


        // Build the form
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("60%");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        TextArea input = new TextArea();
        input.setLabel("Consulta en lenguaje natural");
        input.setPlaceholder("¿cual es el coste del encargo a FUNDUCA para la escuela infantil?");
        formLayout.add(input);

        Button button = new Button("Generar");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formLayout.add(button);

        // Set the layout
        this.add(formLayout);

        add(new H3("Resultado"));
        Grid<EmbeddingMatch<TextSegment>> grid = new Grid();
        grid.addColumn(data -> data.score()).setHeader("Score");
        grid.addColumn(data -> data.embedded().text()).setHeader("Texto");
        grid.addColumn(data -> data.embedded().metadata()).setHeader("Metadatos");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        add(grid);

        // Add the button click listener
        button.addClickListener(e -> {
            String text = input.getValue();
            if (text == null || text.isEmpty()) {
                return;
            }
            List<EmbeddingMatch<TextSegment>> result = vectorSearchService.executeQuery(text);
            grid.setItems(result);
        });


        this.setJustifyContentMode(JustifyContentMode.CENTER); // Vertical alignment
        this.setSizeFull();
        this.setAlignSelf(Alignment.CENTER, formLayout);


    }


}
