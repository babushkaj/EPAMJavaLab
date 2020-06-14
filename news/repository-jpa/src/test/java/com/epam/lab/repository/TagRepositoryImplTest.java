package com.epam.lab.repository;

import com.epam.lab.configuration.RepositoryConfig;
import com.epam.lab.exception.TagNotFoundException;
import com.epam.lab.model.Tag;
import org.junit.Assert;
import org.junit.Ignore;
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
public class TagRepositoryImplTest {

    private static final long TAG_ID = 1L;
    private static final long NONEXISTENT_TAG_ID = 21L;
    private static final String TAG_NAME = "TagOne";
    private static final String UPDATED_TAG_NAME = "UPDATED";

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void shouldSaveNewTag() {
        Tag tag = new Tag();
        tag.setName(TAG_NAME);
        long id = tagRepository.insert(tag);
        Assert.assertEquals(NONEXISTENT_TAG_ID, id);
    }

    @Test
    public void shouldSelectOneTagById() {
        Tag tag = tagRepository.findById(TAG_ID);
        Assert.assertEquals(TAG_NAME, tag.getName());
    }

    @Test(expected = TagNotFoundException.class)
    public void shouldThrowExceptionNoTagWithSuchId() {
        tagRepository.findById(NONEXISTENT_TAG_ID);
    }

    @Test
    public void shouldSelectAllTags() {
        List<Tag> tags = tagRepository.findAll(0, 20);
        Assert.assertNotEquals(0, tags.size());
        Assert.assertEquals(20, tags.size());
    }

    @Test
    public void shouldUpdateTag() {
        Tag tag = new Tag();
        tag.setId(TAG_ID);
        tag.setName(UPDATED_TAG_NAME);
        tagRepository.update(tag);
        Tag updatedTag = tagRepository.findById(TAG_ID);
        Assert.assertEquals(UPDATED_TAG_NAME, updatedTag.getName());
    }

    @Test(expected = TagNotFoundException.class)
    public void shouldThrowExceptionNoCannotUpdateTagWithSuchId() {
        Tag tag = new Tag();
        tag.setId(NONEXISTENT_TAG_ID);
        tag.setName(UPDATED_TAG_NAME);
        tagRepository.update(tag);
    }

    @Test
    public void shouldDeleteTag() {
        tagRepository.delete(TAG_ID);
        List<Tag> tags = tagRepository.findAll(0,20);
        Assert.assertEquals(19, tags.size());
    }

    @Test
    public void shouldFindTagByName() {
        Tag tag = tagRepository.findByName("TagTwo");
        long tagId = tag.getId();
        Assert.assertEquals(2L, tagId);
    }

    @Test
    public void shouldNotFindTagByName() {
        Tag tag = tagRepository.findByName("Tag");
        Assert.assertNull(tag);
    }

}
