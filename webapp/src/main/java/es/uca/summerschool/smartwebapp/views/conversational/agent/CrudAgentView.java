package es.uca.summerschool.smartwebapp.views.conversational.agent;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.agent.CrudAgentService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import es.uca.summerschool.smartwebapp.views.conversational.AbstractConversationalView;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("CRUD Agent")
@Route(value = "conversational/agent/crud", layout = MainLayout.class)
@Menu(order = 16, icon = LineAwesomeIconUrl.ADDRESS_BOOK_SOLID)
public class CrudAgentView extends AbstractConversationalView {

    public CrudAgentView(AuthenticatedUser authenticatedUser, CrudAgentService service) {
        super(authenticatedUser, service);
    }
}
