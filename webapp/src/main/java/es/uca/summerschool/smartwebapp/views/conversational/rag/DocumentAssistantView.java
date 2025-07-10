package es.uca.summerschool.smartwebapp.views.conversational.rag;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.rag.DocumentAssistantService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import es.uca.summerschool.smartwebapp.views.conversational.AbstractConversationalView;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Document Assistant")
@Route(value = "conversational/rag/doc", layout = MainLayout.class)
@Menu(order = 13, icon = LineAwesomeIconUrl.FILE_PDF)
public class DocumentAssistantView extends AbstractConversationalView {

    public DocumentAssistantView(AuthenticatedUser authenticatedUser, DocumentAssistantService service) {
        super(authenticatedUser, service);
    }
}
