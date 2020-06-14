package com.epam.lab.service;

import com.epam.lab.dto.TagDTO;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagDAO;
import com.epam.lab.util.MapperUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.Optional;

@RunWith(JUnit4.class)
public class TagServiceImplTest {

    private static final long TAG_ID = 1L;
    private static final String TAG_NAME = "Tag1";

    private static TagDAO mockTagDAO;
    private static Tag tag1;
    private static TagDTO tagDTO1;
    private static TagService tagService;

    @BeforeClass
    public static void setUp() {
        mockTagDAO = Mockito.mock(TagDAO.class);
        tag1 = new Tag(TAG_ID, TAG_NAME);
        tagDTO1 = MapperUtil.fromTagToTagDTO(tag1);
        Mockito.when(mockTagDAO.select(Mockito.anyLong())).thenReturn(tag1);
        Mockito.when(mockTagDAO.getTagByName(TAG_NAME)).thenReturn(Optional.empty());

        tagService = new TagServiceImpl(mockTagDAO);
    }

    @Test
    public void shouldCallSelectMethodInDAO() {
        tagService.selectTag(1L);
        Mockito.verify(mockTagDAO, Mockito.times(1)).select(1L);
    }

    @Test
    public void shouldCallSelectAllMethodInDAO() {
        tagService.selectAllTags();
        Mockito.verify(mockTagDAO, Mockito.times(1)).selectAll();
    }

    @Test
    public void shouldCallInsertMethodInDAO() {
        tagService.addTag(tagDTO1);
        Mockito.verify(mockTagDAO, Mockito.times(1)).insert(Mockito.anyObject());
    }

    @Test
    public void shouldCallDeleteMethodInDAO() {
        tagService.deleteTag(1L);
        Mockito.verify(mockTagDAO, Mockito.times(1)).delete(1L);
    }

    @Test
    public void shouldCallUpdateMethodInDAO() {
        tagService.updateTag(tagDTO1);
        Mockito.verify(mockTagDAO, Mockito.times(1)).update(tag1);
    }


}
