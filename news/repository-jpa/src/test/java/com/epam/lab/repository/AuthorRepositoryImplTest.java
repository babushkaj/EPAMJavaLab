package com.epam.lab.repository;

import com.epam.lab.configuration.RepositoryConfig;
import com.epam.lab.exception.AuthorNotFoundException;
import com.epam.lab.model.Author;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryConfig.class})
@ActiveProfiles("test")
@Transactional
public class AuthorRepositoryImplTest {
    private static final long AUTHOR_ID = 1L;
    private static final long NONEXISTENT_AUTHOR_ID = 21L;
    private static final String AUTHOR_NAME = "Authorone";
    private static final String AUTHOR_SURNAME = "Firstauthor";
    private static final String UPDATED_AUTHOR_NAME = "Updated";

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void shouldSaveNewAuthor() {
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        author.setSurname(AUTHOR_SURNAME);
        long id = authorRepository.insert(author);
        Assert.assertEquals(NONEXISTENT_AUTHOR_ID, id);
    }

    @Test
    public void shouldSelectOneAuthorById() {
        Author authorFromDB = authorRepository.findById(AUTHOR_ID);
        Author expectedAuthor = new Author();
        expectedAuthor.setId(AUTHOR_ID);
        expectedAuthor.setName(AUTHOR_NAME);
        expectedAuthor.setSurname(AUTHOR_SURNAME);
        Assert.assertEquals(authorFromDB, expectedAuthor);
    }

    @Test(expected = AuthorNotFoundException.class)
    public void shouldThrowExceptionNoAuthorWithSuchId() {
        authorRepository.findById(NONEXISTENT_AUTHOR_ID);
    }

    @Test
    public void shouldSelectAllAuthors() {
        List<Author> authors = authorRepository.findAll(0,20);
        Assert.assertNotEquals(0, authors.size());
    }

    @Test
    public void shouldUpdateAuthor() {
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(UPDATED_AUTHOR_NAME);
        author.setSurname(UPDATED_AUTHOR_NAME);
        authorRepository.update(author);
        Author updatedAuthor = authorRepository.findById(AUTHOR_ID);
        Assert.assertEquals(UPDATED_AUTHOR_NAME, updatedAuthor.getName());
    }

    @Test(expected = AuthorNotFoundException.class)
    public void shouldThrowExceptionCannotUpdateAuthorWithSuchId() {
        Author nonexistentAuthor = new Author();
        nonexistentAuthor.setId(NONEXISTENT_AUTHOR_ID);
        nonexistentAuthor.setName(AUTHOR_NAME);
        nonexistentAuthor.setSurname(AUTHOR_SURNAME);
        authorRepository.update(nonexistentAuthor);
    }

    @Test
    public void shouldDeleteAuthor() {
        authorRepository.delete(AUTHOR_ID);
        List<Author> authors = authorRepository.findAll(0,20);
        Assert.assertEquals(19, authors.size());
    }


}
