package es.uca.summerschool.smartwebapp.services.crud;

import es.uca.summerschool.smartwebapp.data.Book;
import es.uca.summerschool.smartwebapp.data.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Optional<Book> get(Long id) {
        return repository.findById(id);
    }

    public Book save(Book entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Book> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Book> list(Pageable pageable, Specification<Book> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Book> listAll() {
        return repository.findAll();

    }
}
