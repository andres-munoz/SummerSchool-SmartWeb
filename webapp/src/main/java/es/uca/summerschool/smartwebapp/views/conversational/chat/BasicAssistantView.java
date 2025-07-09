package es.uca.summerschool.smartwebapp.views.conversational.chat;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.chat.BasicAssistantService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import es.uca.summerschool.smartwebapp.views.conversational.AbstractConversationalView;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Basic assistant")
@Route(value = "conversational/prompteng/basicassistant", layout = MainLayout.class)
@Menu(order = 8, icon = LineAwesomeIconUrl.UNIVERSITY_SOLID)

public class BasicAssistantView extends AbstractConversationalView {
    public BasicAssistantView(AuthenticatedUser authenticatedUser, BasicAssistantService service) {
        super(authenticatedUser, service);
    }
}
