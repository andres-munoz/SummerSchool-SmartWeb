package es.uca.cursoia.conversational.assistants.bouca;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cursoia.MainLayout;
import es.uca.cursoia.conversational.ConversationalView;
import es.uca.cursoia.conversational.assistants.AssistantService;

@PageTitle("Asistente con RAG naive para el BOUCA")
@Route(value = "assistant/bouca", layout = MainLayout.class)
public class BoucaAssistantView extends ConversationalView {

    protected static final String WELCOME_MESSAGE = """
            Hola %s, soy tu asistente virtual UCA especializado en el BOUCA. ¿En qué puedo ayudarte?
            """;

    private final BoucaAssistantService boucaAssistantService;

    public BoucaAssistantView(BoucaAssistantService boucaAssistantService) {
        this.boucaAssistantService = boucaAssistantService;
    }

    @Override
    protected AssistantService getChatService() {
        return boucaAssistantService;
    }

    @Override
    protected String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }


}
