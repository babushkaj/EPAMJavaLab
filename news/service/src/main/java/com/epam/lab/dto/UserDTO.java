package com.epam.lab.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserDTO {

    @Null(groups = {Saving.class})
    @NotNull(groups = {Updating.class})
    private Long id;

    @NotNull(groups = {Saving.class, Updating.class})
    @Size(min = 1, max = 20, message = "Name must be between 1 and 20 characters",
            groups = {Saving.class, Updating.class})
    private String name;

    @NotNull(groups = {Saving.class, Updating.class})
    @Size(min = 1, max = 20, message = "Surname must be between 1 and 20 characters",
            groups = {Saving.class, Updating.class})
    private String surname;

    @NotNull(groups = {Saving.class, Updating.class})
    @Size(min = 1, max = 30, message = "Login must be between 1 and 30 characters",
            groups = {Saving.class, Updating.class})
    private String login;

    @NotNull(groups = {Saving.class, Updating.class})
    @Size(min = 1, max = 30, message = "Password must be between 1 and 30 characters",
            groups = {Saving.class, Updating.class})
    private String password;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id.equals(userDTO.id) &&
                name.equals(userDTO.name) &&
                surname.equals(userDTO.surname) &&
                login.equals(userDTO.login) &&
                password.equals(userDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, login, password);
    }
}
