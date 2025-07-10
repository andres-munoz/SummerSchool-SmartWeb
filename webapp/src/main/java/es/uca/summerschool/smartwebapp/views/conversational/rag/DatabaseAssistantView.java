package es.uca.summerschool.smartwebapp.views.conversational.rag;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.rag.DatabaseAssistantService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import es.uca.summerschool.smartwebapp.views.conversational.AbstractConversationalView;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Database Assistant")
@Route(value = "conversational/rag/database", layout = MainLayout.class)
@Menu(order = 15, icon = LineAwesomeIconUrl.DATABASE_SOLID)

public class DatabaseAssistantView extends AbstractConversationalView {
    public DatabaseAssistantView(AuthenticatedUser authenticatedUser, DatabaseAssistantService service) {
        super(authenticatedUser, service);
    }
}
