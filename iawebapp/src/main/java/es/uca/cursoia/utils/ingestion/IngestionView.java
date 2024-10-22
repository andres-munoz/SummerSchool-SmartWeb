package es.uca.cursoia.utils.ingestion;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cursoia.MainLayout;
import es.uca.cursoia.WebNotifications;
import es.uca.cursoia.ingestors.bouca.BoucaETL;
import es.uca.cursoia.ingestors.bouca.BoucaIngestionFormView;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;


@PageTitle("Ingestions")
@Route(value = "ingestions", layout = MainLayout.class)
public class IngestionView extends VerticalLayout {

    private final IngestionService ingestionService;
    private final BoucaETL boucaETL;
    private final Grid<Ingestion> grid = new Grid<>(Ingestion.class, false);
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    @Autowired
    BoucaIngestionFormView boucaIngestionFormView;

    private Details controlPanelLayout = new Details("Inyectores de datos");
    private Ingestion entry;
    private TextField description;
    private DateTimePicker lastUpdate;
    private IntegerField numContents;

    public IngestionView(IngestionService ingestionService, BoucaETL boucaETL) {
        this.ingestionService = ingestionService;
        this.boucaETL = boucaETL;

        this.setHeightFull();
        addClassNames("master-detail-view");

        createControlPanelLayout();
        createGrid();

    }

    private void createGrid() {
        add(new H3("Ingestas de datos"));

        // Configure Grid

        // añadir un boton para borrar en cada fila
        grid.addComponentColumn(entry -> {
            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addClickListener(e -> {
                CompletableFuture<Void> future = boucaETL.evict(entry.getSourceId(), entry.getId());

                future.thenAccept((v) -> {
                    WebNotifications.showSuccessNotification("Ingesta suprimida.");
                    grid.setItems(ingestionService.findAll());
                });

                grid.setItems(ingestionService.findAll());
                Notification.show("Suprimiendo ingesta...");
            });
            return deleteButton;
        });

        grid.addColumn(Ingestion::getType).setHeader("Fuente de datos").setAutoWidth(true).setSortable(true);
        grid.addColumn(Ingestion::getNumContents).setHeader("Núm. Contenidos").setAutoWidth(true).setSortable(true);
        grid.addColumn(Ingestion::getSourceId).setHeader("ID Fuente").setAutoWidth(true).setSortable(true);
        grid.addColumn(Ingestion::getDescription).setHeader("Descripción").setAutoWidth(true).setSortable(true);
        grid.addColumn(e -> e.getLastUpdate().format(DateTimeFormatter.ofPattern("dd/MM/yyy hh:mm"))).setHeader("Última actualización").setAutoWidth(true).setSortable(true);


        // permitir reordenar columnas, cambiar ancho de celdas y ordenarlas
        grid.setColumnReorderingAllowed(true);
        grid.setItems(ingestionService.findAll());
        grid.setHeightFull();
        grid.setWidthFull();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        add(grid);

    }


    private void createControlPanelLayout() {

        Button bouca = new Button("Importar BOUCA");
        bouca.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        bouca.addClickListener(e -> {
            boucaIngestionFormView.addInjectionListener(c -> refreshGrid());
            boucaIngestionFormView.open();
        });

        controlPanelLayout.setOpened(true);
        controlPanelLayout.add(bouca);

        add(controlPanelLayout);

        /*
        Button cau = new Button("Importar Servicios del CAU");
        Button directorio = new Button("Importar datos de Personas del Directorio");
        Button webuca = new Button("Importar página web de Wordpress UCA");
        Button asignatura = new Button("Importar datos de asignatura");

        cau.addClickListener(e -> {
            CauInjectorForm boucaIngestionFormView = new CauInjectorForm(this.ingestionService);
            boucaIngestionFormView.addInjectionListener(c -> refreshGrid());
            boucaIngestionFormView.open();
        });


        directorio.addClickListener(e -> {
            DirectorioInjectorForm boucaIngestionFormView = new DirectorioInjectorForm(this.ingestionService);
            boucaIngestionFormView.addInjectionListener(c -> refreshGrid());
            boucaIngestionFormView.open();
        });



        webuca.addClickListener(e -> {
            WebucaInjectorForm boucaIngestionFormView = new WebucaInjectorForm(this.ingestionService);
            boucaIngestionFormView.addInjectionListener(c -> refreshGrid());
            boucaIngestionFormView.open();

        });

        asignatura.addClickListener(e -> {
            AsignaturaInjectorForm boucaIngestionFormView = new AsignaturaInjectorForm(this.ingestionService);
            boucaIngestionFormView.addInjectionListener(c -> refreshGrid());
            boucaIngestionFormView.open();
        });

        // controlPanelLayout.add(cau, directorio, bouca, webuca, asignatura);


        */


    }


    private void refreshGrid() {
        grid.setItems(ingestionService.findAll());
    }


}
