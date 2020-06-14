package com.epam.lab.repository;

import com.epam.lab.model.News;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.time.LocalDate;
import java.util.List;

@RunWith(JUnit4.class)
public class NewsDAOImplTest {

    private static final String CREATION_TABLES_SQL_SCRIPT_NAME = "tables.sql";
    private static final String FILL_TABLES_SQL_SCRIPT_NAME = "data.sql";
    private static final String NEWS_CREATION_DATE = "2018-01-01";
    private static final String NEWS_MODIFICATION_DATE = "2018-02-02";
    private static final long NEWS_ID = 1L;
    private static final String NEWS_TITLE = "The first news";
    private static final String NEWS_SHORT_TEXT = "This is the short text of the first news";
    private static final String NEWS_FULL_TEXT = "This is the very long full text of the first news";
    private static final String NEWS_UPDATED = "UpdatedUpdatedUpdated";

    private NewsDAO newsDAO;
    private EmbeddedDatabase embeddedDatabase;

    @Before
    public void createDatabase() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript(CREATION_TABLES_SQL_SCRIPT_NAME)
                .addScript(FILL_TABLES_SQL_SCRIPT_NAME)
                .setType(EmbeddedDatabaseType.HSQL)
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        newsDAO = new NewsDAOImpl(jdbcTemplate);
    }

    @After
    public void dropDatabase() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void shouldSelectOneNewsById() {
        News newsFromDB = newsDAO.select(NEWS_ID );
        LocalDate creationDate = LocalDate.parse(NEWS_CREATION_DATE);
        LocalDate modificationDate = LocalDate.parse(NEWS_MODIFICATION_DATE);
        News expectedNews = new News(NEWS_ID, NEWS_TITLE, NEWS_SHORT_TEXT, NEWS_FULL_TEXT, creationDate, modificationDate);

        Assert.assertEquals(newsFromDB, expectedNews);
    }

    @Test
    public void shouldSelectAllNews() {
        List<News> news = newsDAO.selectAll();
        Assert.assertEquals(news.size(), 20);
    }

    @Test
    public void shouldAddNewNews() {
        List<News> newsBeforeInserting = newsDAO.selectAll();
        LocalDate creationDate = LocalDate.parse(NEWS_MODIFICATION_DATE);
        LocalDate modificationDate = LocalDate.parse(NEWS_MODIFICATION_DATE);
        News news = new News(0L, NEWS_TITLE, NEWS_SHORT_TEXT, NEWS_FULL_TEXT, creationDate, modificationDate);
        long newNewsId = newsDAO.insert(news);
        List<News> newsAfterInserting = newsDAO.selectAll();
        Assert.assertEquals(newNewsId, 21);
        Assert.assertEquals(newsAfterInserting.size(), newsBeforeInserting.size() + 1);
    }

    @Test
    public void shouldUpdateNews() {
        News newsBeforeUpd = newsDAO.select(NEWS_ID);
        LocalDate creationDate = LocalDate.parse(NEWS_CREATION_DATE);
        LocalDate modificationDate = LocalDate.parse(NEWS_MODIFICATION_DATE);
        News updNews = new News(NEWS_ID, NEWS_UPDATED, NEWS_UPDATED, NEWS_UPDATED, creationDate, modificationDate);
        newsDAO.update(updNews);
        News newsAfterUpd = newsDAO.select(NEWS_ID);

        Assert.assertEquals(newsBeforeUpd.getTitle(), NEWS_TITLE);
        Assert.assertEquals(newsAfterUpd.getTitle(), NEWS_UPDATED);
    }

    @Test
    public void shouldDeleteOneNews() {
        List<News> newsBeforeDeleting = newsDAO.selectAll();
        newsDAO.delete(1L);
        List<News> newsAfterDeleting = newsDAO.selectAll();
        Assert.assertEquals(newsAfterDeleting.size(), newsBeforeDeleting.size() - 1);
    }

    @Test
    public void shouldReturnRightNewsQuantity() {
        long quantity = newsDAO.getNewsCount();
        Assert.assertEquals(quantity, 20L);
    }
}
