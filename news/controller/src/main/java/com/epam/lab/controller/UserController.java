package com.epam.lab.controller;

import com.epam.lab.dto.UserDTO;
import com.epam.lab.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Validated
@CrossOrigin(origins = {"http://localhost:3000", "http://epbyminw8147.minsk.epam.com:3000"})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger("UserController");

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDTO logIn(@RequestBody UserDTO userDTO) {
        UserDTO userFromDB = userService.selectUserByLogin(userDTO.getLogin());
        if (userFromDB != null && userFromDB.getPassword().equals(userDTO.getPassword())) {
            return userFromDB;
        }
        return null;
    }
}
