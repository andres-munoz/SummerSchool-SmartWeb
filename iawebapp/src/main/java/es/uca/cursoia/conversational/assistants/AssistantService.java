package es.uca.cursoia.conversational.assistants;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.service.TokenStream;
import es.uca.cursoia.conversational.assistants.uca.UcaAssistant;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class AssistantService {


    public abstract TokenStream chat(String chatSessionId, String userMessage);

    protected String createSystemMessage(String specificPromptTemplate, String chatSessionId) {

        String promptTemplate =
                specificPromptTemplate +
                        UcaAssistant.SYSTEM_INFO_PROMPT_TEMPLATE +
                        UcaAssistant.USER_INFO_PROMPT_TEMPLATE +
                        UcaAssistant.RESPONSE_PROMPT_TEMPLATE;

        Map<String, Object> variables = new HashMap<>();
        variables.put("fechaHora", LocalDateTime.now());
        variables.put("nombre", "Iván");
        variables.put("apellidos", "Ruiz Rube");
        variables.put("estamento", "Personal Docente e Investigador");
        variables.put("departamento", "Departamento de Ingeniería Informática");
        variables.put("centro", "Escuela Superior de Ingeniería");
        variables.put("ultimaConexion", LocalDateTime.now().minusYears(1));

        Prompt prompt = PromptTemplate.from(promptTemplate).apply(variables);

        return prompt.text();

    }

}
