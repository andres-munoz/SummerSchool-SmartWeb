package es.uca.summerschool.smartwebapp.services.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.springframework.stereotype.Service;

@Service
public class TextHandlerService {

    private ChatModel chatModel;
    private TextHandler textHandler;

    public TextHandlerService(ChatModel chatModel) {
        this.chatModel = chatModel;
        textHandler = AiServices.create(TextHandler.class, chatModel);
    }

    public String summarize(String userMessage, int numWords) {
        return textHandler.summarize(userMessage, numWords);
    }

    public String translate(String userMessage, String language) {
        return textHandler.translate(userMessage, language);
    }

    public Sentiment analyzeSentimentOf(String userMessage) {
        return textHandler.analyzeSentimentOf(userMessage);
    }

    public enum Sentiment {
        POSITIVE, NEUTRAL, NEGATIVE
    }


    public interface TextHandler {

        @UserMessage("You are a professional translator into {{language}}. Translate the following text: {{text}}")
        String translate(@V("text") String text, @V("language") String language);

        @UserMessage("Summarize the following text in no more than {{numWords}} words: {{text}}")
        String summarize(@V("text") String text, @V("numWords") int numWords);

        @UserMessage("Analyze sentiment of the following: {{text}}")
        Sentiment analyzeSentimentOf(@V("text") String text);

    }

}
