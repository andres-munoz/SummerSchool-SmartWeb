package es.uca.cursoia.conversational.assistants;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

public interface Assistant {

    String SYSTEM_INFO_PROMPT_TEMPLATE = """
            Información del sistema: 
                La fecha y hora actual es {{fechaHora}}
            """;
    String USER_INFO_PROMPT_TEMPLATE = """
            Información del usuario: 
                Nombre y apellidos: {{nombre}} {{apellidos}}
                Estamento: {{estamento}}
                Departamento: {{departamento}}
                Centro: {{centro}}
                Última conexión: {{ultimaConexion}}
            """;
    String RESPONSE_PROMPT_TEMPLATE = """
            Formato de la respuesta:
                Deberás responder siempre en español.
                Utiliza un tono educado y respetuoso. 
                Si un usuario realiza preguntas o solicita información al margen de la misión del asistente, responde amablemente 'Lo siento' e indicar que tu función se limita a responder cuestiones sobre la Universidad, y sugiere dirigir la conversación hacia esos temas.                    
            """;

    TokenStream chat(@MemoryId String sessionId, @UserMessage String message);


}
