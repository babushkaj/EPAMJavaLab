package com.epam.lab.repository;

import com.epam.lab.configuration.RepositoryConfig;
import com.epam.lab.exception.IncorrectRequestException;
import com.epam.lab.exception.NewsNotFoundException;
import com.epam.lab.model.News;
import com.epam.lab.specification.NewsSortSpecification;
import com.epam.lab.specification.NewsSpecificationBuilder;
import com.epam.lab.specification.OrderValue;
import com.epam.lab.specification.SearchCriteria;
import com.epam.lab.specification.SortCriteria;
import com.epam.lab.specification.SortSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryConfig.class})
@ActiveProfiles("test")
@Transactional
public class NewsRepositoryImplTest {
    private static final long NEWS_ID = 1L;
    private static final long NONEXISTENT_TAG_ID = 21L;
    private static final String NEWS_TITLE = "The first news";
    private static final String NEWS_SHORT_TEXT = "THIS_IS_NEWS_SHORT_TEXT";
    private static final String NEWS_FULL_TEXT = "THIS_IS_NEWS_FUUUUUULL_TEXT";
    private static final String NEWS_UPDATED_TITLE = "THIS_IS_UPDATED_TITLE";

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void shouldSaveNewNews() {
        News news = new News();
        news.setTitle(NEWS_TITLE);
        news.setShortText(NEWS_SHORT_TEXT);
        news.setFullText(NEWS_FULL_TEXT);
        news.setCreationDate(LocalDate.now());
        news.setModificationDate(LocalDate.now());
        long id = newsRepository.insert(news);
        Assert.assertEquals(NONEXISTENT_TAG_ID, id);
    }

    @Test
    public void shouldSelectOneNewsById() {
        News newsFromDB = newsRepository.findById(NEWS_ID);
        Assert.assertEquals(NEWS_TITLE, newsFromDB.getTitle());
    }

    @Test(expected = NewsNotFoundException.class)
    public void shouldThrowExceptionNoNewsWithSuchId() {
        newsRepository.findById(NONEXISTENT_TAG_ID);
    }

    @Test
    public void shouldSelectAllNews() {
        List<News> news = newsRepository.findAll(0, 20);
        Assert.assertEquals(20, news.size());
    }

    @Test
    public void shouldUpdateNews() {
        News news = newsRepository.findById(NEWS_ID);
        news.setTitle(NEWS_UPDATED_TITLE);
        newsRepository.update(news);
        News updatedNews = newsRepository.findById(NEWS_ID);
        Assert.assertEquals(NEWS_UPDATED_TITLE, updatedNews.getTitle());
    }

    @Test
    public void shouldDeleteNews() {
        newsRepository.delete(NEWS_ID);
        List<News> news = newsRepository.findAll(0, 20);
        Assert.assertEquals(19, news.size());
    }

    @Test
    public void shouldFindNewsByTagAndSortByAuthorName() {
        SearchCriteria sc1 = new SearchCriteria("tags_name", ":", "TagOne");
        NewsSpecificationBuilder newsSpecificationBuilder = new NewsSpecificationBuilder();
        newsSpecificationBuilder.with(sc1);
        SortSpecification<News> sortSpecification = new NewsSortSpecification(
                new SortCriteria("author_name", OrderValue.getOrderValue("asc")));
        List<SortSpecification<News>> sortSpecifications = new ArrayList<>();
        sortSpecifications.add(sortSpecification);
        List<News> news = newsRepository.find(newsSpecificationBuilder.build(), sortSpecifications, 0, 20);
        long firstExpectedNewsId = news.get(0).getId();
        Assert.assertEquals(3, news.size());
        Assert.assertEquals(19, firstExpectedNewsId);
    }

    @Test
    public void shouldFindNewsByAuthorName() {
        SearchCriteria sc1 = new SearchCriteria("author_name", ":", "Authorone");
        NewsSpecificationBuilder newsSpecificationBuilder = new NewsSpecificationBuilder();
        newsSpecificationBuilder.with(sc1);
        List<SortSpecification<News>> sortSpecifications = new ArrayList<>();
        List<News> news = newsRepository.find(newsSpecificationBuilder.build(), sortSpecifications, 0, 20);
        Assert.assertEquals(1, news.size());
    }

    @Test
    public void shouldFindNewsByCreationDate() {
        SearchCriteria sc1 = new SearchCriteria("creationDate", ":", "2018-08-08");
        NewsSpecificationBuilder newsSpecificationBuilder = new NewsSpecificationBuilder();
        newsSpecificationBuilder.with(sc1);
        List<SortSpecification<News>> sortSpecifications = new ArrayList<>();
        List<News> news = newsRepository.find(newsSpecificationBuilder.build(), sortSpecifications, 0, 20);
        Assert.assertEquals(2, news.size());
    }

    @Test
    public void shouldFindNewsByModificationDate() {
        SearchCriteria sc1 = new SearchCriteria("modificationDate", ":", "2018-08-08");
        NewsSpecificationBuilder newsSpecificationBuilder = new NewsSpecificationBuilder();
        newsSpecificationBuilder.with(sc1);
        List<SortSpecification<News>> sortSpecifications = new ArrayList<>();
        List<News> news = newsRepository.find(newsSpecificationBuilder.build(), sortSpecifications, 0, 20);
        Assert.assertEquals(2, news.size());
    }

    @Test(expected = IncorrectRequestException.class)
    public void shouldThrowsIncorrectRequestException() {
        SearchCriteria sc1 = new SearchCriteria("incorrect", ":", "Authorone");
        NewsSpecificationBuilder newsSpecificationBuilder = new NewsSpecificationBuilder();
        newsSpecificationBuilder.with(sc1);
        List<SortSpecification<News>> sortSpecifications = new ArrayList<>();
        newsRepository.find(newsSpecificationBuilder.build(), sortSpecifications, 0, 20);
    }

    @Test
    public void shouldFindNewsByATagAndSortByCreationDate() {
        SearchCriteria sc1 = new SearchCriteria("tags_name", ":", "TagOne");
        NewsSpecificationBuilder newsSpecificationBuilder = new NewsSpecificationBuilder();
        newsSpecificationBuilder.with(sc1);
        SortSpecification<News> sortSpecification = new NewsSortSpecification(
                new SortCriteria("creationDate", OrderValue.getOrderValue("desc")));
        List<SortSpecification<News>> sortSpecifications = new ArrayList<>();
        sortSpecifications.add(sortSpecification);
        List<News> news = newsRepository.find(newsSpecificationBuilder.build(), sortSpecifications, 0, 20);
        long firstExpectedNewsId = news.get(0).getId();
        Assert.assertEquals(3, news.size());
        Assert.assertEquals(20, firstExpectedNewsId);
    }

    @Test
    public void shouldCountNews() {
        long result = newsRepository.countAll();
        Assert.assertEquals(20, result);
    }
}
