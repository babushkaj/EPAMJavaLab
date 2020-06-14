package com.epam.lab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NewsDTO implements Serializable {
    private long serialVersionUID = 1L;

    private Long id;
    @NotNull
    @Size(min = 1, max = 30, message = "Title must be between 1 and 30 characters")
    private String title;
    @NotNull
    @Size(min = 10, max = 200, message = "Short text must be between 10 and 200 characters")
    private String shortText;
    @NotNull
    @Size(min = 20, max = 2000, message = "Full text must be between 20 and 2000 characters")
    private String fullText;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate modificationDate;

    @NotNull(message = "Author cannot be null!")
    private AuthorDTO author;
    private Set<TagDTO> tags = new HashSet<>();

    public NewsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsDTO newsDTO = (NewsDTO) o;
        return id.equals(newsDTO.id) &&
                title.equals(newsDTO.title) &&
                shortText.equals(newsDTO.shortText) &&
                fullText.equals(newsDTO.fullText) &&
                creationDate.equals(newsDTO.creationDate) &&
                modificationDate.equals(newsDTO.modificationDate) &&
                author.equals(newsDTO.author) &&
                tags.equals(newsDTO.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, shortText, fullText, creationDate, modificationDate, author, tags);
    }
}
