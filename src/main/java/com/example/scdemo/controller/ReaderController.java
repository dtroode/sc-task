package com.example.scdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.example.scdemo.entity.Author;
import com.example.scdemo.entity.Book;
import com.example.scdemo.repository.AuthorRepository;
import com.example.scdemo.repository.BookRepository;
import com.example.scdemo.storageservice.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReaderController {
    // Dependency injection.
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    FileStorageService storageService;

    // List books.
    // Accepts different unnecessary params to filter all books.
    // Responds with list of Book entities and HTTP 200 Code or with HTTP 500 Code.
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(value = "year", required = false) Short year,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "author", required = false) List<Author> authors,
            @RequestParam(value = "title", required = false) String title) {
        try {
            List<Book> books = new ArrayList<Book>();

            Stream<Book> stream = bookRepository.findAll().stream();

            // Method finds all books and then applies filters depending on parameters.
            if (year != null)
                stream = stream.filter(b -> b.getYear().equals(year));
            if (genre != null)
                stream = stream.filter(b -> b.getGenre().equals(genre));
            if (publisher != null)
                stream = stream.filter(b -> b.getPublisher().equals(publisher));
            if (authors != null)
                stream = stream.filter(b -> b.getAuthors().containsAll(authors));
            if (title != null)
                stream = stream.filter(b -> b.getTitle().equals(title));

            books = stream.toList();

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get book by id.
    // Responds with Book entity and HTTP 200 Code or with HTTP 404 Code.
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(
            @PathVariable("id") UUID id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get detailed output of book.
    // Responds with Book entity and List of Author enitites and HTTP 200 Code or
    // with HTTP 404 Code.
    @GetMapping("/books/{id}/detail")
    public ResponseEntity<Pair<Book, List<Author>>> getBookDetails(
            @PathVariable("id") UUID id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            Pair<Book, List<Author>> detail;

            List<Author> authors = new ArrayList<Author>();
            book.get().getAuthors().forEach(authors::add);

            // Making and returning a pair with Book and List of Authors.
            detail = Pair.of(book.get(), authors);
            return new ResponseEntity<>(detail, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Method to
    @GetMapping(value = "/books/{id}/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> downloadBook(@PathVariable("id") UUID id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            String filename = book.get().getFile();

            if (filename != null) {
                Resource file = storageService.load(filename);
                HttpHeaders responseHeader = new HttpHeaders();
                responseHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"");
                return new ResponseEntity<>(file, responseHeader, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthors(
            @RequestParam(value = "firstname", required = false) String firstname,
            @RequestParam(value = "lastname", required = false) String lastname,
            @RequestParam(value = "middlename", required = false) String middlename,
            @RequestParam(value = "birth_date", required = false) String birthDate,
            @RequestParam(value = "death_date", required = false) String deathDate) {
        try {
            List<Author> authors = new ArrayList<>();
            Stream<Author> stream = authorRepository.findAll().stream();

            authors = stream.toList();

            return new ResponseEntity<>(authors, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
