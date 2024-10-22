package es.uca;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.github.GitHubModelsChatModel;
import dev.langchain4j.model.github.GitHubModelsStreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("github")
public class GitHubModelsChatModelConfiguration {

    private String GITHUB_TOKEN;

    @Value("${github.token}")
    public void setGithubToken(String token) {
        GITHUB_TOKEN = token;
    }


    @Bean
    ChatLanguageModel gitHubModelsChatLanguageModel() {
        return GitHubModelsChatModel.builder()
                .gitHubToken(GITHUB_TOKEN)
                .modelName("gpt-4o-mini")
                .logRequestsAndResponses(true)
                .build();
    }


    @Bean
    StreamingChatLanguageModel gitHubModelsStreamingChatLanguageModel() {
        return GitHubModelsStreamingChatModel.builder()
                .gitHubToken(GITHUB_TOKEN)
                .modelName("gpt-4o-mini")
                .logRequestsAndResponses(true)
                .build();
    }
}