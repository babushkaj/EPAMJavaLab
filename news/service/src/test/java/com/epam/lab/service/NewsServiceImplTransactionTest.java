package com.epam.lab.service;

import com.epam.lab.configuration.RepositoryConfig;
import com.epam.lab.configuration.ServiceConfig;
import com.epam.lab.dto.NewsDTO;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryConfig.class, ServiceConfig.class, TestConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class NewsServiceImplTransactionTest {

    //    private static final long NEWS_ID = 1L;
    private static final String NEWS_TITLE = "THE NEW NEWS";
    private static final String NEWS_SHORT_TEXT = "THIS IS THE SHORT TEXT OF THE NEW NEWS";
    private static final String NEWS_FULL_TEXT = "THIS IS THE VERY LONG FULL TEXT OF THE NEW NEWS";
    private static final String NEWS_CREATION_DATE = "2018-11-11";
    private static final String NEWS_MODIFICATION_DATE = "2018-12-12";
    private static final long TAG_ID = 99L;
    private static final String TAG_NAME = "TagTagTag";
    private static final long AUTHOR_ID = 1L;
    private static final String AUTHOR_NAME = "AuthorOne";
    private static final String AUTHOR_SURNAME = "Firstauthor";

    private static NewsDTO newsDTO1;

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private TagRepository tagRepository;

    @Before
    public void setUp() {
        LocalDate creationDate = LocalDate.parse(NEWS_CREATION_DATE);
        LocalDate modificationDate = LocalDate.parse(NEWS_MODIFICATION_DATE);
        News news1 = new News();
        news1.setTitle(NEWS_TITLE);
        news1.setShortText(NEWS_SHORT_TEXT);
        news1.setFullText(NEWS_FULL_TEXT);
        news1.setCreationDate(creationDate);
        news1.setModificationDate(modificationDate);
        Author author1 = new Author();
        author1.setId(AUTHOR_ID);
        author1.setName(AUTHOR_NAME);
        author1.setSurname(AUTHOR_SURNAME);
        Tag tag1 = new Tag();
        tag1.setId(TAG_ID);
        tag1.setName(TAG_NAME);
        news1.setAuthor(author1);
        Set<Tag> tags = new HashSet<>();
        tags.add(tag1);
        news1.setTags(tags);
        MapperUtil mapperUtil = new MapperUtil(new ModelMapper());
        newsDTO1 = mapperUtil.convertNewsToDTO(news1);
    }

    @Test
    public void shouldRollbackTransactionalAddNewsMethodWithoutChanges() {
        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenThrow(RuntimeException.class);
        long newsCountBeforeInsert = newsRepository.findAll().size();
        try {
            newsService.addNews(newsDTO1);
        } catch (RuntimeException e) {
        }
        long newsCountAfterInsert = newsRepository.findAll().size();
        Assert.assertEquals(newsCountBeforeInsert, newsCountAfterInsert);
    }
}
