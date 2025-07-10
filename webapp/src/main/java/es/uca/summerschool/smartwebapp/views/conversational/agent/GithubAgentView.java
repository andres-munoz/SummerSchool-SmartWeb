package es.uca.summerschool.smartwebapp.views.conversational.agent;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.summerschool.smartwebapp.security.AuthenticatedUser;
import es.uca.summerschool.smartwebapp.services.conversational.agent.GithubAgentService;
import es.uca.summerschool.smartwebapp.views.MainLayout;
import es.uca.summerschool.smartwebapp.views.conversational.AbstractConversationalView;
import org.springframework.context.annotation.Profile;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Github Agent")
@Route(value = "conversational/agent/github", layout = MainLayout.class)
@Menu(order = 17, icon = LineAwesomeIconUrl.GITHUB)
@Profile("mcp")
public class GithubAgentView extends AbstractConversationalView {

    public GithubAgentView(AuthenticatedUser authenticatedUser, GithubAgentService service) {
        super(authenticatedUser, service);
    }
}
