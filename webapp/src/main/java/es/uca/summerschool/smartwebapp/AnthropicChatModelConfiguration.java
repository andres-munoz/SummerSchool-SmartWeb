package es.uca.summerschool.smartwebapp;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.anthropic.AnthropicStreamingChatModel;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static dev.langchain4j.model.anthropic.AnthropicChatModelName.CLAUDE_3_5_SONNET_20240620;

@Configuration
@Profile("anthropic")
public class AnthropicChatModelConfiguration {

    @Value("${anthropic.token}")
    private String anthropicToken;

    @Bean
    ChatModel anthropicChatModel() {
        return AnthropicChatModel.builder()
                .apiKey(anthropicToken)
                .modelName(CLAUDE_3_5_SONNET_20240620)
                .logRequests(true)
                .logResponses(true)
                .build();
    }


    @Bean
    AnthropicStreamingChatModel anthropicStreamingChatModel() {
        return AnthropicStreamingChatModel.builder()
                .apiKey(anthropicToken)
                .modelName(CLAUDE_3_5_SONNET_20240620)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}