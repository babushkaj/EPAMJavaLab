package com.epam.lab.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class TagDTO implements Serializable {
    private long serialVersionUID = 1L;

    private Long id;
    @NotNull
    @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
    private String name;

    public TagDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return id.equals(tagDTO.id) &&
                name.equals(tagDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
