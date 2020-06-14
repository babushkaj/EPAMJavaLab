package com.epam.lab.repository;

import com.epam.lab.exception.TagNotFoundException;
import com.epam.lab.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static com.epam.lab.repository.DAOConstants.*;

@Repository
public class TagDAOImpl implements TagDAO {

    private static final String READ_TAG = "SELECT * FROM tag WHERE id = ?";
    private static final String READ_ALL_TAGS = "SELECT * FROM tag";
    private static final String INSERT_TAG = "INSERT INTO tag (name) VALUES (?)";
    private static final String UPDATE_TAG = "UPDATE tag SET name = ? WHERE id = ?";
    private static final String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    private static final String GET_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?";
    private static final String DELETE_RELATIONS_TAG_TO_NEWS = "DELETE FROM news_tag WHERE tag_id = ?";

    private static final String READ_TAGS_BY_NEWS_ID = "SELECT * FROM tag " +
            " INNER JOIN news_tag ON news_tag.tag_id = tag.id WHERE news_tag.news_id = ?";

    private static final String TAG_WITH_ID = "Tag with id = ";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long insert(Tag entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_TAG, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            return ps;
        }, keyHolder);
        return (long) keyHolder.getKeys().get(ID);
    }

    @Override
    public Tag select(long id) {
        try {
            return jdbcTemplate.queryForObject(READ_TAG, TagDAO.tagRowMapper(), id);
        } catch (DataAccessException e) {
            throw new TagNotFoundException(TAG_WITH_ID + id + IS_NOT_FOUND, e);
        }
    }

    @Override
    public List<Tag> selectAll() {
        return jdbcTemplate.query(READ_ALL_TAGS, TagDAO.tagRowMapper());
    }

    @Override
    public void update(Tag entity) {
        int updatedRowsCount = jdbcTemplate.update(UPDATE_TAG, entity.getName(), entity.getId());
        if (updatedRowsCount == 0) {
            throw new TagNotFoundException(TAG_WITH_ID + entity.getId() + IS_NOT_FOUND + AND_CANNOT_BE_UPDATED);
        }
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        Optional<Tag> returnedTag;
        try {
            Tag tag = jdbcTemplate.queryForObject(GET_TAG_BY_NAME, TagDAO.tagRowMapper(), tagName);
            returnedTag = Optional.of(tag);
        } catch (DataAccessException e) {
            returnedTag = Optional.empty();
        }

        return returnedTag;
    }

    @Override
    public void deleteRelationTagToNews(long tagId) {
        jdbcTemplate.update(DELETE_RELATIONS_TAG_TO_NEWS, tagId);
    }

    @Override
    public List<Tag> getAllTagsByNewsId(Long newsId) {
        return jdbcTemplate.query(READ_TAGS_BY_NEWS_ID, TagDAO.tagRowMapper(), newsId);
    }
}
