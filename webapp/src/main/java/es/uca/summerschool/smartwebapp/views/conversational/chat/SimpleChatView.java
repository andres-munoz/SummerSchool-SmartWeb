package es.uca.summerschool.smartwebapp.views.conversational.chat;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.chat.SimpleChatService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import es.uca.summerschool.smartwebapp.views.conversational.AbstractConversationalView;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Chat simple")
@Route(value = "conversational/chat/simple", layout = MainLayout.class)
@Menu(order = 6, icon = LineAwesomeIconUrl.SPEAKER_DECK)

public class SimpleChatView extends AbstractConversationalView {


    public SimpleChatView(AuthenticatedUser authenticatedUser, SimpleChatService service) {
        super(authenticatedUser, service);
    }

}
