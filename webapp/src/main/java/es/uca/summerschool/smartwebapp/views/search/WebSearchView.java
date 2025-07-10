package es.uca.summerschool.smartwebapp.views.search;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.summerschool.smartwebapp.services.conversational.rag.WebAssistantService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Web Search")
@Route(value = "search/web", layout = MainLayout.class)
@Menu(order = 11, icon = LineAwesomeIconUrl.INTERNET_EXPLORER)
@AnonymousAllowed

public class WebSearchView extends AbstractSearchView {
    public WebSearchView(WebAssistantService webAssistantService) {

        super(webAssistantService);
        this.input.setPlaceholder("summer school");

    }


}
