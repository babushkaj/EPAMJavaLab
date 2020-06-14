package com.epam.lab.service;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.model.Author;
import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.util.MapperUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class AuthorServiceImplTest {

    private static final long AUTHOR_ID = 1L;
    private static final String AUTHOR_NAME = "Author1";

    private AuthorRepository mockAuthorDAO;
    private AuthorService authorService;
    private Author author1;
    private AuthorDTO authorDTO1;

    @Before
    public void setUp() {
        MapperUtil mapperUtil = new MapperUtil(new ModelMapper());
        mockAuthorDAO = mock(AuthorRepository.class);
        author1 = new Author();
        author1.setId(AUTHOR_ID);
        author1.setName(AUTHOR_NAME);
        author1.setSurname(AUTHOR_NAME);
        authorDTO1 = mapperUtil.convertAuthorToDTO(author1);

        when(mockAuthorDAO.findById(anyLong())).thenReturn(author1);

        authorService = new AuthorServiceImpl(mockAuthorDAO, mapperUtil);
    }

    @Test
    public void shouldCallFindByIdMethodInAuthorRepository() {
        authorService.selectAuthor(1L);
        verify(mockAuthorDAO, times(1)).findById(1L);
    }

    @Test
    public void shouldCallFindAllMethodInAuthorRepository() {
        authorService.selectAuthors(0,20);
        verify(mockAuthorDAO, times(1)).findAll(0, 20);
    }

    @Test
    public void shouldCallInsertMethodInAuthorRepository() {
        authorService.addAuthor(authorDTO1);
        verify(mockAuthorDAO, times(1)).insert(anyObject());
    }

    @Test
    public void shouldCallDeleteMethodInAuthorRepository() {
        authorService.deleteAuthor(1L);
        verify(mockAuthorDAO, times(1)).delete(1L);
    }

    @Test
    public void shouldCallUpdateMethodInAuthorRepository() {
        authorService.updateAuthor(authorDTO1);
        verify(mockAuthorDAO, times(1)).update(author1);
    }

}

