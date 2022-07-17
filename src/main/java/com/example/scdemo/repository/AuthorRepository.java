package com.example.scdemo.repository;

import java.util.List;
import java.util.UUID;

import com.example.scdemo.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    List<Author> findAuthorsByBooksId(UUID bookId);

    List<Author> findByFirstnameAndLastname(String firstname, String lastname);

    List<Author> findByFirstname(String firstname);

    List<Author> findByLastname(String lastname);

    List<Author> findByBirthDate(String birthDate);

    List<Author> findByDeathDate(String deathDate);

    List<Author> findByDescriptionContaining(String description);
}
