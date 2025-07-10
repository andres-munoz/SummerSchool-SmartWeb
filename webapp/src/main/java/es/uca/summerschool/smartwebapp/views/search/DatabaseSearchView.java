package es.uca.summerschool.smartwebapp.views.search;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.summerschool.smartwebapp.services.conversational.rag.DatabaseAssistantService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Database Search")
@Route(value = "search/database", layout = MainLayout.class)
@Menu(order = 12, icon = LineAwesomeIconUrl.DATABASE_SOLID)
@AnonymousAllowed
public class DatabaseSearchView extends AbstractSearchView {

    private Grid<List<String>> grid;

    public DatabaseSearchView(DatabaseAssistantService databaseSearchService) {
        super(databaseSearchService);
        this.input.setPlaceholder("Which books were published in 2014?");
    }


}
