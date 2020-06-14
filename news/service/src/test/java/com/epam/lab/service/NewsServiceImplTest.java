package com.epam.lab.service;

import com.epam.lab.dto.NewsDTO;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.repository.AuthorDAO;
import com.epam.lab.repository.NewsDAO;
import com.epam.lab.repository.TagDAO;
import com.epam.lab.util.MapperUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(JUnit4.class)
public class NewsServiceImplTest {

    private static final String NEWS_CREATION_DATE = "2018-11-11";
    private static final String NEWS_MODIFICATION_DATE = "2018-12-12";
    private static final long AUTHOR_ID = 1L;
    private static final String AUTHOR_NAME = "TestAuthor";
    private static final long NEWS_ID = 1L;
    private static final String NEWS_TITLE = "The first news";
    private static final String NEWS_SHORT_TEXT = "This is the short text of the first news";
    private static final String NEWS_FULL_TEXT = "This is the very long full text of the first news";

    private static NewsDAO mockNewsDAO;
    private static TagDAO mockTagDAO;
    private static AuthorDAO mockAuthorDAO;
    private static News news1;
    private static NewsDTO newsDTO1;
    private static NewsService newsService;

    @Before
    public void setUp() {
        mockNewsDAO = Mockito.mock(NewsDAO.class);
        mockTagDAO = Mockito.mock(TagDAO.class);
        mockAuthorDAO = Mockito.mock(AuthorDAO.class);
        LocalDate creationDate = LocalDate.parse(NEWS_CREATION_DATE);
        LocalDate modificationDate = LocalDate.parse(NEWS_MODIFICATION_DATE);
        news1 = new News(NEWS_ID, NEWS_TITLE, NEWS_SHORT_TEXT, NEWS_FULL_TEXT, creationDate, modificationDate);
        newsDTO1 = MapperUtil.fromNewsToNewsDTO(news1);
        Author author = new Author(AUTHOR_ID, AUTHOR_NAME, AUTHOR_NAME);
        newsDTO1.setAuthor(MapperUtil.fromAuthorToAuthorDTO(author));

        Mockito.when(mockNewsDAO.select(Mockito.anyLong())).thenReturn(news1);
        Mockito.when(mockTagDAO.getTagByName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(mockTagDAO.insert(Mockito.any())).thenReturn(1L);
        Mockito.when(mockAuthorDAO.insert(Mockito.any())).thenReturn(1L);
        Mockito.when(mockAuthorDAO.getAuthorByNewsId(Mockito.anyLong())).thenReturn(new Author());

        newsService = new NewsServiceImpl(mockNewsDAO, mockTagDAO, mockAuthorDAO,
                new SpecificationBuilderImpl(new SearchSpecificationFactory(), new SortSpecificationFactory()));
    }

    @Test
    public void shouldCallSelectMethodInDAO() {
        newsService.selectNews(1L);
        Mockito.verify(mockNewsDAO, Mockito.times(1)).select(1L);
    }

    @Test
    public void shouldCallInsertMethodInDAO() {
        newsService.addNews(newsDTO1);
        Mockito.verify(mockNewsDAO, Mockito.times(1)).insert(Mockito.anyObject());
    }

    @Test
    public void shouldCallDeleteMethodInDAO() {
        newsService.deleteNews(1L);
        Mockito.verify(mockNewsDAO, Mockito.times(1)).delete(1L);
    }

    @Test
    public void shouldCallUpdateMethodInDAO() {
        newsService.updateNews(newsDTO1);
        news1.setModificationDate(newsDTO1.getModificationDate());
        Mockito.verify(mockNewsDAO, Mockito.times(1)).update(news1);
    }

}
