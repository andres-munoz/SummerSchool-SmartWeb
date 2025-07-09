package es.uca.summerschool.smartwebapp.views.conversational.chat;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.chat.MemoryChatService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import es.uca.summerschool.smartwebapp.views.conversational.AbstractConversationalView;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Chat with memory")
@Route(value = "conversational/chat/memory", layout = MainLayout.class)
@Menu(order = 7, icon = LineAwesomeIconUrl.MEMORY_SOLID)

public class MemoryChatView extends AbstractConversationalView {


    public MemoryChatView(AuthenticatedUser authenticatedUser, MemoryChatService service) {
        super(authenticatedUser, service);
    }

}
