package com.epam.lab.service;

import com.epam.lab.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO selectUser(long id);

    List<UserDTO> selectUsers(int from, int howMany);

    void deleteUser(long id);

    UserDTO updateUser(UserDTO user);

    UserDTO addUser(UserDTO user);

    Long count();
//
//    UserDTO selectUserByLogin(String login);
}
