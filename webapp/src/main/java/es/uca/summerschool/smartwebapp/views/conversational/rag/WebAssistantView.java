package es.uca.summerschool.smartwebapp.views.conversational.rag;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.rag.WebAssistantService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import es.uca.summerschool.smartwebapp.views.conversational.AbstractConversationalView;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Web Assistant")
@Route(value = "conversational/rag/web", layout = MainLayout.class)
@Menu(order = 14, icon = LineAwesomeIconUrl.INTERNET_EXPLORER)
@AnonymousAllowed

public class WebAssistantView extends AbstractConversationalView {
    public WebAssistantView(AuthenticatedUser authenticatedUser, WebAssistantService service) {
        super(authenticatedUser, service);
    }
}
