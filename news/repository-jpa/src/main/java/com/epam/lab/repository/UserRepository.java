package com.epam.lab.repository;

import com.epam.lab.model.User;

public interface UserRepository extends AbstractRepository<User> {
    User findByLogin(String login);
}
