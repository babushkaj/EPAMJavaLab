package com.epam.lab.service;

import com.epam.lab.dto.AuthorDTO;

import java.util.List;

public interface AuthorService {
    AuthorDTO selectAuthor(long id);

    List<AuthorDTO> selectAuthors(int from, int howMany);

    void deleteAuthor(long id);

    AuthorDTO updateAuthor(AuthorDTO author);

    AuthorDTO addAuthor(AuthorDTO author);

    Long count();
}
