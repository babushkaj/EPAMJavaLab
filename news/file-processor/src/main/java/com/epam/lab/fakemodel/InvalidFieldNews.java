package com.epam.lab.fakemodel;

import com.epam.lab.model.Author;
import com.epam.lab.model.Tag;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class InvalidFieldNews {

    private Long invalidField;

    private String title;

    private String shortText;

    private String fullText;

    private LocalDate creationDate;

    private LocalDate modificationDate;

    private Author author;

    private Set<Tag> tags = new HashSet<>();

    public InvalidFieldNews() {
    }

    public InvalidFieldNews(Long invalidField, String title, String shortText, String fullText,
                            LocalDate creationDate, LocalDate modificationDate, Author author) {
        this.invalidField = invalidField;
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.author = author;
    }

    public Long getInvalidField() {
        return invalidField;
    }

    public void setInvalidField(Long invalidField) {
        this.invalidField = invalidField;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvalidFieldNews that = (InvalidFieldNews) o;
        return Objects.equals(invalidField, that.invalidField) &&
                Objects.equals(title, that.title) &&
                Objects.equals(shortText, that.shortText) &&
                Objects.equals(fullText, that.fullText) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(modificationDate, that.modificationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invalidField, title, shortText, fullText, creationDate, modificationDate);
    }

    @Override
    public String toString() {
        return "InvalidFieldNews{" +
                "invalidField=" + invalidField +
                ", title='" + title + '\'' +
                ", shortText='" + shortText + '\'' +
                ", fullText='" + fullText + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", author=" + author +
                ", tags=" + tags +
                '}';
    }
}
