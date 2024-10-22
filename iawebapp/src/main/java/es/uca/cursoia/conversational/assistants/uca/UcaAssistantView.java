package es.uca.cursoia.conversational.assistants.uca;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cursoia.MainLayout;
import es.uca.cursoia.conversational.ConversationalView;
import es.uca.cursoia.conversational.assistants.AssistantService;

@PageTitle("Asistente para la UCA sin RAG")
@Route(value = "assistant/uca", layout = MainLayout.class)
public class UcaAssistantView extends ConversationalView {

    protected static final String WELCOME_MESSAGE = """
            Hola %s, soy tu asistente virtual de la Universidad de Cádiz. ¿En qué puedo ayudarte?
            """;

    private final UcaAssistantService ucaAssistantService;

    public UcaAssistantView(UcaAssistantService ucaAssistantService) {
        this.ucaAssistantService = ucaAssistantService;
    }

    @Override
    protected AssistantService getChatService() {
        return ucaAssistantService;
    }

    @Override
    protected String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }


}