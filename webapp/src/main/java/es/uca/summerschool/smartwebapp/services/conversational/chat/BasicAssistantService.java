package es.uca.summerschool.smartwebapp.services.conversational.chat;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.service.AiServices;
import es.uca.summerschool.smartwebapp.services.conversational.Conversational;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class BasicAssistantService implements Conversational {

    protected static final String WELCOME_MESSAGE_TEMPLATE = """
            Hi %s. I am the UCA intelligent assistant, here to help you with all your academic needs.
            """;

    protected static final String SPECIFIC_PROMPT_TEMPLATE = """
            Role:
                You are an AI assistant created by the University of Cadiz (UCA) to help members of the university community.
                Your mission is to provide information to the user about teaching, research, and management aspects of UCA.
            Tasks:
                1. Analyze the query posed by the user.
                2. Respond to the query including the URL of the source from where you obtain the information.
            """;

    protected static final String SYSTEM_INFO_PROMPT_TEMPLATE = """
            System Information:
                The current date and time is {{currentDateTime}}
            """;

    protected static final String USER_INFO_PROMPT_TEMPLATE = """
            User Information:
                Full name: {{firstName}} {{lastName}}
                Position: {{position}}
                Department: {{department}}
                Faculty: {{faculty}}
                Last login: {{lastConnection}}
            """;

    protected static final String RESPONSE_PROMPT_TEMPLATE = """
            Response Format:
                You must always respond in same language of the user.
                Use a polite and respectful tone.
                If a user asks questions or requests information outside the assistant's mission, kindly respond 'I'm sorry' and indicate that your function is limited to answering questions about the University, and suggest redirecting the conversation towards those topics.
            """;

    private final Conversational conversationalService;


    public BasicAssistantService(StreamingChatModel streamingChatModel) {
        conversationalService = AiServices.builder(Conversational.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> createSystemMessage((String) chatSessionId))
                .build();
    }

    public String createSystemMessage(String chatSessionId) {

        String promptTemplate =
                SPECIFIC_PROMPT_TEMPLATE +
                        SYSTEM_INFO_PROMPT_TEMPLATE +
                        USER_INFO_PROMPT_TEMPLATE +
                        RESPONSE_PROMPT_TEMPLATE;

        Map<String, Object> variables = new HashMap<>();
        variables.put("currentDateTime", LocalDateTime.now());
        variables.put("firstName", "Iván");
        variables.put("lastName", "Ruiz Rube");
        variables.put("position", "Teaching and Research Staff");
        variables.put("department", "Department of Computer Engineering");
        variables.put("faculty", "School of Engineering");
        variables.put("lastConnection", LocalDateTime.now().minusWeeks(1));

        Prompt prompt = PromptTemplate.from(promptTemplate).apply(variables);

        return prompt.text();

    }

    @Override
    public Flux<String> chat(String chatSessionId, String userMessage) {
        return conversationalService.chat(chatSessionId, userMessage);
    }

    @Override
    public String getWelcomeMessage(String username) {
        return WELCOME_MESSAGE_TEMPLATE.formatted(username);
    }
}
