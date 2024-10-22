package es.uca.cursoia.search.databasesearch;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cursoia.MainLayout;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Búsqueda semántica en base de datos de TITULACIONES")
@Route(value = "databasesearch", layout = MainLayout.class)
public class DatabaseSearchView extends VerticalLayout {
    public DatabaseSearchView(DatabaseSearchService databaseSearchService) {
        super();

        // Build the form
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("60%");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        TextArea input = new TextArea();
        input.setLabel("Consulta en lenguaje natural");
        input.setPlaceholder("¿Cuántas titulaciones hay?");
        input.setValue("Dame toda la información de las titulaciones");
        formLayout.add(input);

        Button button = new Button("Generar");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formLayout.add(button);

        // Set the layout
        this.add(formLayout);


        add(new H3("Resultados"));
        add(new Grid());

        // Add the button click listener
        button.addClickListener(e -> {

            // Borramos si hubiera algun grid en el formulario
            this.getChildren().forEach(component -> {
                if (component instanceof Grid) {
                    this.remove(component);
                }
            });

            // Hacemos la consulta
            String text = input.getValue();
            List<List<String>> result = databaseSearchService.executeNLQuery(text);

            // Obtenemos las columnas
            Grid<List<String>> grid = new Grid();

            // Vemos que columnas hay
            List<String> cols = new ArrayList<>();
            for (String col : result.get(0)) {
                cols.add(col);
            }
            // Añadimos las columnas
            for (int i = 0; i < result.get(0).size(); i++) {
                int finalI = i;
                grid.addColumn(row -> row.get(finalI)).setHeader(cols.get(i)).setAutoWidth(true);
            }

            // añadimos los resultados excepto la primera fila
            grid.setItems(result.subList(1, result.size()));
            add(grid);


        });


        this.setJustifyContentMode(JustifyContentMode.CENTER); // Vertical alignment
        this.setSizeFull();
        this.setAlignSelf(Alignment.CENTER, formLayout);


    }


}
