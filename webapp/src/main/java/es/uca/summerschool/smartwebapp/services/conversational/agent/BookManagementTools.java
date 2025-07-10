package es.uca.summerschool.smartwebapp.services.conversational.agent;

import dev.langchain4j.agent.tool.Tool;
import es.uca.summerschool.smartwebapp.data.Book;
import es.uca.summerschool.smartwebapp.services.crud.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookManagementTools {

    private final BookService bookService;

    public BookManagementTools(BookService bookService) {
        this.bookService = bookService;
    }

    @Tool
    public Optional<Book> get(Long id) {
        return bookService.get(id);
    }

    @Tool
    public Book save(Book entity) {
        System.out.println("Calling save with entity: " + entity);
        return bookService.save(entity);
    }

    @Tool
    public void delete(Long id) {
        System.out.println("Calling delete with id: " + id);
        bookService.delete(id);
    }


    @Tool(name = "list", value = "List all books")
    public List<Book> list() {
        System.out.println("Calling list all books");
        return bookService.listAll();
    }

    @Tool(name = "listWithFilter", value = "List books with filter")
    public Page<Book> list(Pageable pageable, Specification<Book> filter) {
        System.out.println("Calling list with pageable: " + pageable + " and filter: " + filter);
        return bookService.list(pageable, filter);
    }

    @Tool
    public int count() {
        System.out.println("Calling count");
        return bookService.count();
    }


}
