package com.example.scdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.*;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "middlename")
    private String middlename;

    @Column(name = "birth_date")
    private String birthDate;
    @Column(name = "death_date")
    private String deathDate;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "authors")
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

    public Author() {
    }

    public Author(UUID id) {
        this.id = id;
    }

    public Author(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Author(String firstname, String lastname, String middlename,
            String birthDate, String deathDate,
            String description) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;

        this.birthDate = birthDate;
        this.deathDate = deathDate;

        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public String getDescription() {
        return description;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }
}
