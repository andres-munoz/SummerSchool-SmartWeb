package es.uca.summerschool.smartwebapp;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.github.GitHubModelsChatModel;
import dev.langchain4j.model.github.GitHubModelsStreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("github")
public class GitHubModelsChatModelConfiguration {

    @Value("${github.token}")
    private String gitHubToken;

    @Bean
    ChatModel gitHubModelsChatModel() {
        return GitHubModelsChatModel.builder()
                .gitHubToken(gitHubToken)
                .modelName("gpt-4o-mini")
                .logRequestsAndResponses(true)
                .build();
    }


    @Bean
    GitHubModelsStreamingChatModel gitHubModelsStreamingChatModel() {
        return GitHubModelsStreamingChatModel.builder()
                .gitHubToken(gitHubToken)
                .modelName("gpt-4o-mini")
                .logRequestsAndResponses(true)
                .build();
    }
}