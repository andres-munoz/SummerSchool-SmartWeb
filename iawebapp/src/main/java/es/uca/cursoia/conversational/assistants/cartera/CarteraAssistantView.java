package es.uca.cursoia.conversational.assistants.cartera;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.cursoia.MainLayout;
import es.uca.cursoia.conversational.ConversationalView;
import es.uca.cursoia.conversational.assistants.AssistantService;

@PageTitle("Asistente con RAG fácil para la Cartera de Proyectos")
@Route(value = "assistant/cartera", layout = MainLayout.class)
public class CarteraAssistantView extends ConversationalView {

    protected static final String WELCOME_MESSAGE = """
            Hola %s, soy tu asistente virtual UCA especializado en la cartera estratégica de proyectos TI. ¿En qué puedo ayudarte?
            """;

    private final CarteraAssistantService carteraAssistantService;

    public CarteraAssistantView(CarteraAssistantService carteraAssistantService) {
        this.carteraAssistantService = carteraAssistantService;
    }

    @Override
    protected AssistantService getChatService() {
        return carteraAssistantService;
    }

    @Override
    protected String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }


}
