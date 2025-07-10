package es.uca.summerschool.smartwebapp.views.search;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.summerschool.smartwebapp.services.conversational.rag.DocumentAssistantService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Document Search")
@Route(value = "search/doc", layout = MainLayout.class)
@Menu(order = 10, icon = LineAwesomeIconUrl.FILE_PDF)
@AnonymousAllowed

public class DocumentSearchView extends AbstractSearchView {
    public DocumentSearchView(DocumentAssistantService docAssistantService) {
        super(docAssistantService);
        this.input.setPlaceholder("What are the contents of the course?");

    }


}
