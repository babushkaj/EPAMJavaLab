package com.epam.lab.controller;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO selectAuthorById(@PathVariable("id") long id) {
        return authorService.selectAuthor(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorDTO> selectAllAuthors() {
        return authorService.selectAllAuthors();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAuthor(@PathVariable("id") long id) {
        authorService.deleteAuthor(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO editAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        return authorService.updateAuthor(authorDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO addAuthor(@Valid @RequestBody AuthorDTO authorDTO, HttpServletRequest request,
                               HttpServletResponse response) {
        AuthorDTO savedAuthorDTO = authorService.addAuthor(authorDTO);
        long authorId = savedAuthorDTO.getId();
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + "/" + authorId;
        response.addHeader("Location", uri);
        return savedAuthorDTO;
    }
}
