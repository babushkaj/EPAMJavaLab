package com.epam.lab.service;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.model.Author;
import com.epam.lab.repository.AuthorDAO;
import com.epam.lab.repository.NewsDAO;
import com.epam.lab.util.MapperUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class AuthorServiceImplTest {

    private static final long AUTHOR_ID = 1L;
    private static final String AUTHOR_NAME = "Author1";

    private static AuthorDAO mockAuthorDAO;
    private static NewsDAO mockNewsDAO;
    private static AuthorService authorService;
    private static Author author1;
    private static AuthorDTO authorDTO1;

    @BeforeClass
    public static void setUp() {
        mockAuthorDAO = Mockito.mock(AuthorDAO.class);
        mockNewsDAO = Mockito.mock(NewsDAO.class);
        author1 = new Author(AUTHOR_ID, AUTHOR_NAME, AUTHOR_NAME);
        authorDTO1 = MapperUtil.fromAuthorToAuthorDTO(author1);

        Mockito.when(mockAuthorDAO.select(Mockito.anyLong())).thenReturn(author1);

        authorService = new AuthorServiceImpl(mockAuthorDAO, mockNewsDAO);
    }

    @Test
    public void shouldCallSelectMethodInDAO() {
        authorService.selectAuthor(1L);
        Mockito.verify(mockAuthorDAO, Mockito.times(1)).select(1L);
    }

    @Test
    public void shouldCallSelectAllMethodInDAO() {
        authorService.selectAllAuthors();
        Mockito.verify(mockAuthorDAO, Mockito.times(1)).selectAll();
    }

    @Test
    public void shouldCallInsertMethodInDAO() {
        authorService.addAuthor(authorDTO1);
        Mockito.verify(mockAuthorDAO, Mockito.times(1)).insert(Mockito.anyObject());
    }

    @Test
    public void shouldCallDeleteMethodInDAO() {
        authorService.deleteAuthor(1L);
        Mockito.verify(mockAuthorDAO, Mockito.times(1)).delete(1L);
    }

    @Test
    public void shouldCallUpdateMethodInDAO() {
        authorService.updateAuthor(authorDTO1);
        Mockito.verify(mockAuthorDAO, Mockito.times(1)).update(author1);
    }

}

