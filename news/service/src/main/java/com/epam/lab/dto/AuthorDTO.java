package com.epam.lab.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class AuthorDTO implements Serializable {
    private long serialVersionUID = 1L;

    private Long id;
    @NotNull
    @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
    private String name;
    @NotNull
    @Size(min = 1, max = 30, message = "Surname must be between 1 and 30 characters")
    private String surname;

    public AuthorDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDTO authorDTO = (AuthorDTO) o;
        return id.equals(authorDTO.id) &&
                name.equals(authorDTO.name) &&
                surname.equals(authorDTO.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname);
    }
}
