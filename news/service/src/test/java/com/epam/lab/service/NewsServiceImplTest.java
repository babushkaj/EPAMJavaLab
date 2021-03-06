package com.epam.lab.service;

import com.epam.lab.dto.NewsDTO;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.specification.OrderValue;
import com.epam.lab.specification.SearchCriteria;
import com.epam.lab.specification.SortCriteria;
import com.epam.lab.util.MapperUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class NewsServiceImplTest {
    private static final long NEWS_ID = 1L;
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

    private static NewsRepository mockNewsRepository;
    private static NewsDTO newsDTO1;

    private NewsService newsService;

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
        newsDTO1 = spy(mapperUtil.convertNewsToDTO(news1));

        mockNewsRepository = mock(NewsRepository.class);
        AuthorRepository mockAuthorRepository = mock(AuthorRepository.class);
        TagRepository mockTagRepository = mock(TagRepository.class);
        when(mockNewsRepository.findById(anyLong())).thenReturn(news1);
        when(mockNewsRepository.insert(any(News.class))).thenReturn(21L);
        when(newsDTO1.getId()).thenReturn(1L);
        when(mockAuthorRepository.insert(any(Author.class))).thenReturn(21L);
        when(mockTagRepository.insert(any(Tag.class))).thenReturn(21L);
        when(mockTagRepository.findByName(anyString())).thenReturn(null);
        when(mockNewsRepository.countAll()).thenReturn(20L);

        newsService = new NewsServiceImpl(mockNewsRepository, mockAuthorRepository, mockTagRepository, mapperUtil);
    }

    @Test
    public void shouldCallFindByIdMethodInNewsRepository() {
        newsService.selectNews(NEWS_ID);
        verify(mockNewsRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldCallInsertMethodInNewsRepository() {
        NewsDTO newsDTO = newsService.addNews(newsDTO1);
        verify(mockNewsRepository, times(1))
                .insert(any());
    }

    @Test
    public void shouldCallDeleteMethodInNewsRepository() {
        newsService.deleteNews(1L);
        verify(mockNewsRepository, times(1)).delete(1L);
    }

    @Test
    public void shouldCallUpdateMethodInNewsRepository() {
        newsService.updateNews(newsDTO1);
        verify(mockNewsRepository, times(1)).update(any());
    }

    @Test
    public void shouldCallCountNewsMethodInNewsRepository() {
        newsService.count(new ArrayList<>());
        verify(mockNewsRepository, times(1)).countAll();
    }

    @Test
    public void shouldCallFindAllMethodInNewsRepository() {
        List<SearchCriteria> emptySearchCriteria = new ArrayList<>();
        List<SortCriteria> emptySortCriteria = new ArrayList<>();
        newsService.findNews(emptySearchCriteria, emptySortCriteria, 0, 20);
        verify(mockNewsRepository, times(1)).findAll(0, 20);
    }

    @Test
    public void shouldCallFindMethodInNewsRepository() {
        SearchCriteria tagNameSearchCriteria = new SearchCriteria("tags_name", ":", "TagOne");
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(tagNameSearchCriteria);
        List<SortCriteria> sortCriteria = new ArrayList<>();
        SortCriteria authorNameSortCriteria = new SortCriteria("author_name", OrderValue.ASC);
        sortCriteria.add(authorNameSortCriteria);
        newsService.findNews(searchCriteria, sortCriteria, 0, 20);
        verify(mockNewsRepository, times(1)).find(any(), anyList(), eq(0), eq(20));
    }

}
