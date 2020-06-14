package com.epam.lab.service;


import com.epam.lab.dto.TagDTO;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.util.MapperUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

@RunWith(JUnit4.class)
public class TagServiceImplTest {

    private static final long TAG_ID = 1L;
    private static final String TAG_NAME = "Tag1";
    private static final String NEW_TAG_NAME = "NewTag";

    private TagRepository mockTagDAO;
    private Tag tag1;
    private Tag newTag;
    private TagDTO tagDTO1;
    private TagDTO newTagDTO1;
    private TagService tagService;

    @Before
    public void setUp() {
        MapperUtil mapperUtil = new MapperUtil(new ModelMapper());
        mockTagDAO = Mockito.mock(TagRepository.class);
        tag1 = new Tag();
        tag1.setId(TAG_ID);
        tag1.setName(TAG_NAME);
        newTag = new Tag(NEW_TAG_NAME);
        tagDTO1 = mapperUtil.convertTagToDTO(tag1);
        newTagDTO1 = mapperUtil.convertTagToDTO(newTag);
        Mockito.when(mockTagDAO.findById(Mockito.anyLong())).thenReturn(tag1);
        Mockito.when(mockTagDAO.findByName(Mockito.any())).thenReturn(null);

        tagService = new TagServiceImpl(mockTagDAO, mapperUtil);
    }

    @Test
    public void shouldCallFindByIdMethodInTagRepository() {
        tagService.selectTag(1L);
        Mockito.verify(mockTagDAO, Mockito.times(1)).findById(1L);
    }

    @Test
    public void shouldCallFindAllMethodInTagRepository() {
        tagService.selectAllTags();
        Mockito.verify(mockTagDAO, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldCallInsertMethodInTagRepository() {
        tagService.addTag(newTagDTO1);
        Mockito.verify(mockTagDAO, Mockito.times(1)).insert(Mockito.anyObject());
    }

    @Test
    public void shouldCallDeleteMethodInTagRepository() {
        tagService.deleteTag(1L);
        Mockito.verify(mockTagDAO, Mockito.times(1)).delete(1L);
    }

    @Test
    public void shouldCallUpdateMethodInTagRepository() {
        tagService.updateTag(tagDTO1);
        Mockito.verify(mockTagDAO, Mockito.times(1)).update(tag1);
    }

}
