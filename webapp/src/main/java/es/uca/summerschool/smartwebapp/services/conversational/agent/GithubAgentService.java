package es.uca.summerschool.smartwebapp.services.conversational.agent;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import es.uca.summerschool.smartwebapp.services.conversational.Conversational;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Profile("mcp")
public class GithubAgentService implements Conversational {

    protected static final String WELCOME_MESSAGE = """
            Hi %s, I am a virtual assistant able to communicate with Github. What can I help you?
            """;

    protected static final String SYSTEM_PROMPT_TEMPLATE = """
            You are an AI-powered assistant to use Github. 
            """;

    private final StreamingChatModel streamingChatModel;
    private final Conversational service;
    private ContentRetriever contentRetriever;

    public GithubAgentService(StreamingChatModel streamingChatModel, @Value("${mcp.github.url}") String url,
                              @Value("${mcp.github.token}") String token) {
        this.streamingChatModel = streamingChatModel;

        service = AiServices.builder(Conversational.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(chatSessionId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(chatSessionId -> SYSTEM_PROMPT_TEMPLATE)
                .toolProvider(githubTools(url, token))
                .build();
    }


    private ToolProvider githubTools(String url, String token) {


        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("docker", "run",
                        "-e", "GITHUB_PERSONAL_ACCESS_TOKEN=" + token,
                        "-e", "GITHUB_TOOLSETS=context,issues",
                        "-i",
                        "ghcr.io/github/github-mcp-server"))
                .logEvents(true)
                .build();

/*
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl(url)
                .logRequests(true)
                .logResponses(true)
                .build();
*/
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .key("MyMCPClient023023548238555")
                .transport(transport)
                .build();

        // Print the available tools
        mcpClient.listTools().forEach(tool -> System.out.println("Available tool: " + tool.name()));

        // Print the available prompts
        mcpClient.listPrompts().forEach(prompt -> System.out.println("Available prompt: " + prompt.name()));

        // Print the available resources
        mcpClient.listResources().forEach(resource -> System.out.println("Available resource: " + resource.name()));

        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();

        return toolProvider;

    }


    @Override
    public Flux<String> chat(String chatSessionId, String userMessage) {
        return service.chat(chatSessionId, userMessage);
    }

    @Override
    public String getWelcomeMessage(String username) {
        return WELCOME_MESSAGE.formatted(username);
    }

}