package com.epam.lab.controller;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.dto.Saving;
import com.epam.lab.dto.Updating;
import com.epam.lab.service.AuthorService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/authors")
@Validated
@CrossOrigin("http://localhost:3000")
public class AuthorController {
    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO selectAuthorById(@PathVariable("id")
                                      @Min(value = 1, message = "Author ID must be greater or equal to 1")
                                              long id) {
        return authorService.selectAuthor(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorDTO> selectAllAuthors() {
        return authorService.selectAllAuthors();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAuthor(@PathVariable("id")
                             @Min(value = 1, message = "Author ID must be greater or equal to 1")
                                     long id) {
        authorService.deleteAuthor(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO editAuthor(@Validated(Updating.class) @RequestBody AuthorDTO authorDTO) {
        return authorService.updateAuthor(authorDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO addAuthor(@Validated(Saving.class) @RequestBody AuthorDTO authorDTO, HttpServletRequest request,
                               HttpServletResponse response) {
        AuthorDTO savedAuthorDTO = authorService.addAuthor(authorDTO);
        long authorId = savedAuthorDTO.getId();
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + "/" + authorId;
        response.addHeader("Location", uri);
        return savedAuthorDTO;
    }
}
