package com.example.scdemo.controller;

import java.util.Optional;
import java.util.UUID;

import com.example.scdemo.entity.Author;
import com.example.scdemo.entity.Book;
import com.example.scdemo.helper.ResponseMessage;
import com.example.scdemo.repository.AuthorRepository;
import com.example.scdemo.repository.BookRepository;
import com.example.scdemo.storageservice.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class EditorController {
    // Dependency injection.
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    FileStorageService storageService;

    // Add book endpoint.
    // Accepts book as JSON and converts it to class by @RequestBody ability.
    // Responds with Book entity and HTTP 201 Code or with HTTP 500 Code.
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        try {
            // Create new book entity.
            Book _book = bookRepository.save(new Book(book.getTitle(), book.getYear(), book.getGenre(),
                    book.getPages(), book.getPublisher()));

            book.getAuthors().forEach(author -> {
                UUID authorId = author.getId();

                // For each author check if he has ID.
                if (authorId != null) {
                    // If author has ID that means he's must be already in database.
                    // So we need to find author entity controlled by Spring by it's ID.
                    Author _author = authorRepository.findById(authorId)
                            .orElseThrow();

                    // And add author to the book entity with saving entity to Context.
                    _book.addAuthor(_author);
                    bookRepository.save(_book);
                } else {
                    // If author doesn't have ID, we just add it to book entity and
                    // save entity to Context.
                    // Because of our Cascade settings when parent (book) entity is saved
                    // child entity, author will also be saved.
                    _book.addAuthor(author);
                    authorRepository.save(author);
                }
            });

            return new ResponseEntity<>(_book, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add book file to existing book.
    // Accepts book id and book file.
    // If book presents, updates entity by adding file.
    // Responds with Book entity and HTTP 200 Code or with HTTP 404 Code.
    @PostMapping("/books/{id}/upload")
    public ResponseEntity<ResponseMessage> uploadBookfile(@PathVariable("id") UUID id,
            @RequestParam(value = "file") MultipartFile file) {
        Optional<Book> book = bookRepository.findById(id);
        String message = "";

        if (book.isPresent()) {
            try {
                // Storage service saves file to defined folder.
                storageService.save(file);
                message = "File uploaded successfully: " + file.getOriginalFilename() + ".";

                // Next we update book with filename and save changes to Context.
                Book _book = book.get();
                _book.addFile(file.getOriginalFilename());
                bookRepository.save(_book);

                return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.OK);
            } catch (Exception e) {
                message = "Could not upload file: " + file.getOriginalFilename() + ".";
                return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            message = "Book not found.";
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.NOT_FOUND);
        }
    }

    // Remove book.
    // Accepts id of book to remove.
    // Responds with HTTP 204 Code or with HTTP 500 Code.
    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") UUID id) {
        try {
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add author.
    // Accepts author as JSON and converts it to class by @RequestBody ability.
    // Responds with Author entity and HTTP 201 Code or with HTTP 500 Code.
    @PostMapping("/authors")
    public ResponseEntity<Author> addAuthor(@RequestBody Author author) {
        try {
            Author _author = authorRepository.save(new Author(author.getFirstname(), author.getLastname(),
                    author.getMiddlename(), author.getBirthDate(), author.getDeathDate(), author.getDescription()));
            return new ResponseEntity<>(_author, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Remove author.
    // Accepts id of author to remove.
    // Author won't be removed until there's books pointed to this author.
    // This behaviour controlled by Cascade settings in Author entity in
    // Many-to-many relationship.
    // Responds with HTTP 204 Code or with HTTP 500 Code.
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<HttpStatus> deleteAuthor(@PathVariable("id") UUID id) {
        try {
            authorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
