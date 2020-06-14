package com.epam.lab.repository;

import com.epam.lab.model.Tag;
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

import java.util.List;
import java.util.Optional;

@RunWith(JUnit4.class)
public class TagDAOImplTest {

    private static final String CREATION_TABLES_SQL_SCRIPT_NAME = "tables.sql";
    private static final String FILL_TABLES_SQL_SCRIPT_NAME = "data.sql";
    private static final long TAG_ID = 1L;
    private static final String TAG_NAME = "TagOne";
    private static final String NEW_TAG_NAME = "NEW_TAG";
    private static final String UPDATED_TAG_NAME = "Updated";
    private static final String NON_EXISTENT_TAG_NAME = "TagNoExist";

    private TagDAO tagDAO;
    private EmbeddedDatabase embeddedDatabase;

    @Before
    public void createDatabase() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript(CREATION_TABLES_SQL_SCRIPT_NAME)
                .addScript(FILL_TABLES_SQL_SCRIPT_NAME)
                .setType(EmbeddedDatabaseType.HSQL)
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        tagDAO = new TagDAOImpl(jdbcTemplate);
    }

    @After
    public void dropDatabase() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void shouldSelectOneTagById() {
        Tag tagFromDB = tagDAO.select(TAG_ID);
        Tag expectedTag = new Tag(TAG_ID, TAG_NAME);
        Assert.assertEquals(tagFromDB, expectedTag);
    }

    @Test
    public void shouldSelectAllTags() {
        List<Tag> tags = tagDAO.selectAll();
        Assert.assertEquals(tags.size(), 20);
    }

    @Test
    public void shouldAddNewTag() {
        List<Tag> tagsBeforeInserting = tagDAO.selectAll();
        Tag tag = new Tag();
        tag.setName(NEW_TAG_NAME);
        tagDAO.insert(tag);
        List<Tag> tagsAfterInserting = tagDAO.selectAll();
        Assert.assertEquals(tagsAfterInserting.size(), tagsBeforeInserting.size() + 1);
    }

    @Test
    public void shouldUpdateTag() {
        Tag tagBeforeUpd = tagDAO.select(TAG_ID);
        Tag updTag = new Tag(TAG_ID, UPDATED_TAG_NAME );
        tagDAO.update(updTag);
        Tag tagAfterUpd = tagDAO.select(TAG_ID);

        Assert.assertEquals(tagBeforeUpd.getName(), TAG_NAME );
        Assert.assertEquals(tagAfterUpd.getName(), UPDATED_TAG_NAME );
    }

    @Test
    public void shouldDeleteOneTag() {
        List<Tag> tagsBeforeDeleting = tagDAO.selectAll();
        tagDAO.delete(1L);
        List<Tag> tagsAfterDeleting = tagDAO.selectAll();
        Assert.assertEquals(tagsAfterDeleting.size(), tagsBeforeDeleting.size() - 1);
    }

    @Test
    public void shouldFindAllTagsByNewsId() {
        List<Tag> tags = tagDAO.getAllTagsByNewsId(1L);
        Assert.assertEquals(tags.size(), 3L);
    }

    @Test
    public void shouldFindTagsByNewsIdAfterDeletingNewsTagRelation() {
        tagDAO.deleteRelationTagToNews(1L);
        List<Tag> tags = tagDAO.getAllTagsByNewsId(1L);
        Assert.assertEquals(tags.size(), 2L);
    }

    @Test
    public void shouldFindTagByName() {
        Tag tag = tagDAO.getTagByName(TAG_NAME).get();
        long tagId = tag.getId();
        Assert.assertEquals(tagId, 1L);
    }

    @Test
    public void shouldNotFindTagByName() {
        Optional<Tag> tagOptional = tagDAO.getTagByName(NON_EXISTENT_TAG_NAME);
        Assert.assertFalse(tagOptional.isPresent());
    }


}
