package es.uca.cursoia.conversational.assistants.uca;

import dev.langchain4j.service.TokenStream;
import es.uca.cursoia.conversational.ConversationalController;
import es.uca.cursoia.conversational.ConversationalRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Servicios web de IA
 */
@RestController
public class UcaAssistantRestController extends ConversationalController {

    private final UcaAssistantService ucaAssistantService;

    public UcaAssistantRestController(UcaAssistantService ucaAssistantService) {
        this.ucaAssistantService = ucaAssistantService;
    }


    /**
     * Este servicio te permite realizar una petición al asistente virtual
     *
     * @param request Datos de la petición
     * @return Respuesta del asistente
     */
    @PostMapping("/api/v1/assistants/norag")
    public Flux<String> chat(@Valid @RequestBody ConversationalRequest request) {
        TokenStream aux = ucaAssistantService.chat(request.getChatSessionId(), request.getMessage());
        return convertTokenStream2Flux(aux);
    }


}
