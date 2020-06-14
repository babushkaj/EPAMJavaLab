package com.epam.lab.repository;

import com.epam.lab.model.Author;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

@RunWith(JUnit4.class)
public class AuthorDAOImplTest {

    private static final String CREATION_TABLES_SQL_SCRIPT_NAME = "tables.sql";
    private static final String FILL_TABLES_SQL_SCRIPT_NAME = "data.sql";
    private static final long AUTHOR_ID = 1L;
    private static final String AUTHOR_NAME = "Authorone";
    private static final String AUTHOR_SURNAME = "Firstauthor";
    private static final String UPDATED_AUTHOR_NAME = "Updated";

    private AuthorDAO authorDAO;
    private EmbeddedDatabase embeddedDatabase;

    @Before
    public void createDatabase() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript(CREATION_TABLES_SQL_SCRIPT_NAME)
                .addScript(FILL_TABLES_SQL_SCRIPT_NAME)
                .setType(EmbeddedDatabaseType.HSQL)
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        authorDAO = new AuthorDAOImpl(jdbcTemplate);
    }

    @After
    public void dropDatabase() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void shouldSelectOneAuthorById() {
        Author authorFromDB = authorDAO.select(AUTHOR_ID);
        Author expectedAuthor = new Author(AUTHOR_ID, AUTHOR_NAME, AUTHOR_SURNAME);
        Assert.assertEquals(authorFromDB, expectedAuthor);
    }

    @Test
    public void shouldSelectAllAuthors() {
        List<Author> authors = authorDAO.selectAll();
        Assert.assertEquals(authors.size(), 20);
    }

    @Test
    public void shouldAddNewAuthor() {
        List<Author> authorsBeforeInserting = authorDAO.selectAll();
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        author.setSurname(AUTHOR_SURNAME);
        authorDAO.insert(author);
        List<Author> authorsAfterInserting = authorDAO.selectAll();
        Assert.assertEquals(authorsAfterInserting.size(), authorsBeforeInserting.size() + 1);
    }

    @Test
    public void shouldUpdateAuthor() {
        Author authorBeforeUpd = authorDAO.select(AUTHOR_ID);
        Author updAuthor = new Author(AUTHOR_ID, UPDATED_AUTHOR_NAME, UPDATED_AUTHOR_NAME);
        authorDAO.update(updAuthor);
        Author authorAfterUpd = authorDAO.select(AUTHOR_ID);

        Assert.assertEquals(authorBeforeUpd.getName(), AUTHOR_NAME);
        Assert.assertEquals(authorAfterUpd.getName(), UPDATED_AUTHOR_NAME);
    }

    @Test
    public void shouldDeleteOneAuthor() {
        List<Author> authorsBeforeDeleting = authorDAO.selectAll();
        authorDAO.delete(1L);
        List<Author> authorsAfterDeleting = authorDAO.selectAll();
        Assert.assertEquals(authorsAfterDeleting.size(), authorsBeforeDeleting.size() - 1);
    }

    @Test
    public void shouldFindAllNewsIdByAuthorId() {
        List<Long> newsId = authorDAO.getAllNewsIdForAuthor(1L);
        Assert.assertEquals(newsId.size(), 1L);
    }

    @Test
    public void shouldFindAuthorByNewsId() {
        Author author = authorDAO.getAuthorByNewsId(1L);
        long authorId = author.getId();
        Assert.assertEquals(authorId, 1L);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldFindTagsByNewsIdAfterDeletingNewsTagRelation() {
        authorDAO.deleteRelationAuthorToNews(1L);
        Author author = authorDAO.getAuthorByNewsId(1L);
        Assert.assertNull(author);
    }

}

