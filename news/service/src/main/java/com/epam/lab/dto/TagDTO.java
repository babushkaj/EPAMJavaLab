package com.epam.lab.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class TagDTO implements Serializable {
    static final long serialVersionUID = 1L;

    @Null(groups = {Saving.class})
    @NotNull(groups = {Updating.class})
    private Long id;
    @NotBlank(groups = {Saving.class, Updating.class})
    @Size(max = 30, message = "Name must be between 1 and 30 characters",
            groups = {Saving.class, Updating.class})
    private String name;

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

    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
