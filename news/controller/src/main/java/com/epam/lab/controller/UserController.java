package com.epam.lab.controller;

import com.epam.lab.dto.Saving;
import com.epam.lab.dto.Updating;
import com.epam.lab.dto.UserDTO;
import com.epam.lab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
@CrossOrigin(origins = {"http://localhost:3000", "http://epbyminw8147.minsk.epam.com:3000"},
        allowedHeaders = {"users-amount", "page-number"})
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO selectUserById(@PathVariable("id")
                                  @Min(value = 1, message = "User ID must be greater or equal to 1")
                                          long id) {
        return userService.selectUser(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> selectAllUsers(@RequestParam int pageNumber, @RequestParam int howMany,
                                        HttpServletResponse response) {
        int from = pageNumber * howMany;
        long totalNumber = userService.count();
        if (from >= totalNumber) {
            if (totalNumber % howMany == 0) {
                from = (int) (totalNumber - howMany);
                pageNumber = (int) totalNumber / howMany - 1;
            } else {
                from = (int) (totalNumber - (totalNumber % howMany));
                pageNumber = (int) totalNumber / howMany;
            }
        }

        response.addHeader("users-amount", String.valueOf(totalNumber));
        response.addHeader("page-number", String.valueOf(pageNumber));
        return userService.selectUsers(from, howMany);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id")
                           @Min(value = 1, message = "User ID must be greater or equal to 1")
                                   long id) {
        userService.deleteUser(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDTO editUser(@Validated(Updating.class) @RequestBody UserDTO user) {
        return userService.updateUser(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@Validated(Saving.class) @RequestBody UserDTO userDTO, HttpServletRequest request,
                           HttpServletResponse response) {
        UserDTO savedUserDTO = userService.addUser(userDTO);
        long userId = savedUserDTO.getId();
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + "/" + userId;
        response.addHeader("Location", uri);
        return savedUserDTO;
    }
}
