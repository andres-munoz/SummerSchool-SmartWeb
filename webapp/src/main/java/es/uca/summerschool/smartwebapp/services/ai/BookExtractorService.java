package es.uca.summerschool.smartwebapp.services.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import es.uca.summerschool.smartwebapp.data.Book;
import org.springframework.stereotype.Service;

@Service
public class BookExtractorService {

    private ChatModel chatModel;
    private BookExtractor bookExtractor;

    public BookExtractorService(ChatModel chatModel) {
        this.chatModel = chatModel;
        bookExtractor = AiServices.create(BookExtractor.class, chatModel);
    }

    public Book extractBookFrom(String userMessage) {
        return bookExtractor.extractBookFrom(userMessage);
    }

    public interface BookExtractor {
        @UserMessage("Extract information about a book from {{text}}")
        Book extractBookFrom(@V("text") String text);
    }


}
