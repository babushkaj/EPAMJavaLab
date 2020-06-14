package com.epam.lab.repository;

import com.epam.lab.exception.NewsNotFoundException;
import com.epam.lab.model.News;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static com.epam.lab.repository.DAOConstants.*;

@Repository
public class NewsDAOImpl implements NewsDAO {

    private static final String READ_NEWS = "SELECT * FROM news WHERE id = ?";
    private static final String READ_ALL_NEWS = "SELECT * FROM news";
    private static final String INSERT_NEWS = "INSERT INTO news (title, short_text, full_text, creation_date, " +
            "modification_date) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_NEWS = "UPDATE news SET title = ?, short_text = ?, full_text = ?, creation_date = ?, " +
            "modification_date = ? WHERE id = ?";
    private static final String DELETE_NEWS = "DELETE FROM news WHERE id = ?";
    private static final String RELATION_NEWS_TO_AUTHOR = "INSERT INTO news_author (news_id, author_id) VALUES (?, ?)";
    private static final String RELATION_NEWS_TO_TAG = "INSERT INTO news_tag (news_id, tag_id) VALUES (?, ?)";
    private static final String DELETE_RELATION_NEWS_TO_AUTHOR = "DELETE FROM news_author WHERE news_id = ?";
    private static final String DELETE_RELATION_NEWS_TO_TAG = "DELETE FROM news_tag WHERE news_id = ?";

    private static final String SELECT_NEWS = "SELECT news.id, news.title, news.short_text, news.full_text, " +
            "news.creation_date, news.modification_date, tag.id, tag.name, author.id, author.name, author.surname " +
            "FROM news " +
            "JOIN news_tag ON news.id = news_tag.news_id " +
            "JOIN tag ON news_tag.tag_id = tag.id " +
            "JOIN news_author ON news.id = news_author.news_id " +
            "JOIN author ON news_author.author_id = author.id " +
            "WHERE 1 = 1 ";

    private static final String COUNT_NEWS = "SELECT COUNT(*) FROM news";

    private static final String NEWS_WITH_ID = "News with id = ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String COMMA_AND_SPACE = ", ";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public NewsDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long insert(News entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_NEWS, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getShortText());
            ps.setString(3, entity.getFullText());
            ps.setDate(4, Date.valueOf(entity.getCreationDate()));
            ps.setDate(5, Date.valueOf(entity.getModificationDate()));
            return ps;
        }, keyHolder);
        return (long) keyHolder.getKeys().get(ID);
    }

    @Override
    public News select(long id) {
        try {
            return jdbcTemplate.queryForObject(READ_NEWS, NewsDAO.newsRowMapper(), id);
        } catch (DataAccessException e) {
            throw new NewsNotFoundException(NEWS_WITH_ID + id + IS_NOT_FOUND, e);
        }
    }

    @Override
    public List<News> selectAll() {
        return jdbcTemplate.query(READ_ALL_NEWS, NewsDAO.newsRowMapper());
    }

    @Override
    public void update(News entity) {
        int updatedRowsCount = jdbcTemplate.update(UPDATE_NEWS, entity.getTitle(), entity.getShortText(),
                entity.getFullText(), entity.getCreationDate(), entity.getModificationDate(), entity.getId());
        if (updatedRowsCount == 0) {
            throw new NewsNotFoundException(NEWS_WITH_ID + entity.getId() + IS_NOT_FOUND +
                    AND_CANNOT_BE_UPDATED);
        }
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_NEWS, id);
    }

    @Override
    public void setRelationNewsToAuthor(long newsId, long authorId) {
        jdbcTemplate.update(RELATION_NEWS_TO_AUTHOR, newsId, authorId);
    }

    @Override
    public void setRelationNewsToTag(long newsId, long tagId) {
        jdbcTemplate.update(RELATION_NEWS_TO_TAG, newsId, tagId);
    }

    @Override
    public void deleteRelationNewsToAuthor(long newsId) {
        jdbcTemplate.update(DELETE_RELATION_NEWS_TO_AUTHOR, newsId);
    }

    @Override
    public void deleteRelationNewsToAllTags(long newsId) {
        jdbcTemplate.update(DELETE_RELATION_NEWS_TO_TAG, newsId);
    }

    @Override
    public List<News> find(List<SearchSpecification> searchSpecifications, List<SortSpecification> sortSpecifications) {
        String selectionConditions = buildSearchSpecification(searchSpecifications);
        String sortingConditions = buildSortSpecification(sortSpecifications);
        return jdbcTemplate.query(SELECT_NEWS + selectionConditions + sortingConditions, NewsDAO.newsRowMapper());
    }

    private String buildSearchSpecification(List<SearchSpecification> searchSpecifications) {
        StringBuilder selectionConditions = new StringBuilder();
        if (searchSpecifications != null && !searchSpecifications.isEmpty()) {
            for (SearchSpecification s : searchSpecifications) {
                selectionConditions.append(s.getSql());
            }
        }
        return selectionConditions.toString();
    }

    private String buildSortSpecification(List<SortSpecification> sortSpecifications) {
        StringBuilder sortingConditions = new StringBuilder();
        if (sortSpecifications != null && !sortSpecifications.isEmpty()) {
            sortingConditions.append(ORDER_BY);
            for (SortSpecification ss : sortSpecifications) {
                sortingConditions.append(ss.getSql());
                sortingConditions.append(COMMA_AND_SPACE);
            }
            sortingConditions.delete(sortingConditions.length() - 2, sortingConditions.length());
        }
        return sortingConditions.toString();
    }


    @Override
    public Long getNewsCount() {
        return jdbcTemplate.queryForObject(COUNT_NEWS, new Object[]{}, Long.class);
    }
}
