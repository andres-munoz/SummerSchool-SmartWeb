package es.uca.cursoia.conversational.assistants.redmine;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cursoia.MainLayout;
import es.uca.cursoia.conversational.ConversationalView;
import es.uca.cursoia.conversational.assistants.AssistantService;

@PageTitle("Asistente para el Redmine de la UCA con acceso a herramientas")
@Route(value = "assistant/redmine", layout = MainLayout.class)
public class RedmineAssistantView extends ConversationalView {

    protected static final String WELCOME_MESSAGE = """
            Hola %s, soy tu asistente virtual UCA especializado en el Redmine... ¿En qué puedo ayudarte?
            """;

    private final RedmineAssistantService redmineAssistantService;

    public RedmineAssistantView(RedmineAssistantService redmineAssistantService) {
        this.redmineAssistantService = redmineAssistantService;
    }

    @Override
    protected AssistantService getChatService() {
        return redmineAssistantService;
    }

    @Override
    protected String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }


}
