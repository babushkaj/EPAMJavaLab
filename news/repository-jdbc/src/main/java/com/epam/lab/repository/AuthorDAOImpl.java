package com.epam.lab.repository;

import com.epam.lab.exception.AuthorNotFoundException;
import com.epam.lab.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static com.epam.lab.repository.DAOConstants.*;

@Repository
public class AuthorDAOImpl implements AuthorDAO {

    private static final String READ_AUTHOR = "SELECT * FROM author WHERE id = ?";
    private static final String READ_ALL_AUTHORS = "SELECT * FROM author";
    private static final String INSERT_AUTHOR = "INSERT INTO author (name, surname) VALUES (?, ?)";
    private static final String UPDATE_AUTHOR = "UPDATE author SET name = ?, surname = ? WHERE id = ?";
    private static final String DELETE_AUTHOR = "DELETE FROM author WHERE id = ?";
    private static final String DELETE_RELATIONS_AUTHOR_TO_NEWS = "DELETE FROM news_author WHERE author_id = ?";
    private static final String GET_ALL_NEWS_ID_FOR_AUTHOR = "SELECT news_author.news_id FROM news_author " +
            "INNER JOIN author ON news_author.author_id = author.id WHERE author.id = ?";

    private static final String READ_AUTHOR_BY_NEWS_ID = "SELECT * FROM author " +
            "INNER JOIN news_author ON news_author.author_id = author.id WHERE news_author.news_id = ?";

    private static final String AUTHOR_WITH_ID = "Author with id = ";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long insert(Author entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getSurname());
            return ps;
        }, keyHolder);
        return (long) keyHolder.getKeys().get(ID);
    }

    @Override
    public Author select(long id) {
        try {
            return jdbcTemplate.queryForObject(READ_AUTHOR, AuthorDAO.authorRowMapper(), id);
        } catch (DataAccessException e) {
            throw new AuthorNotFoundException(AUTHOR_WITH_ID + id + IS_NOT_FOUND, e);
        }
    }

    @Override
    public List<Author> selectAll() {
        return jdbcTemplate.query(READ_ALL_AUTHORS, AuthorDAO.authorRowMapper());
    }

    @Override
    public void update(Author entity) {
        int updatedRowsCount = jdbcTemplate.update(UPDATE_AUTHOR, entity.getName(), entity.getSurname(), entity.getId());
        if (updatedRowsCount == 0) {
            throw new AuthorNotFoundException(AUTHOR_WITH_ID + entity.getId() + IS_NOT_FOUND + AND_CANNOT_BE_UPDATED);
        }
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_AUTHOR, id);
    }

    @Override
    public void deleteRelationAuthorToNews(long authorId) {
        jdbcTemplate.update(DELETE_RELATIONS_AUTHOR_TO_NEWS, authorId);
    }

    @Override
    public List<Long> getAllNewsIdForAuthor(Long authorId) {
        return jdbcTemplate.queryForList(GET_ALL_NEWS_ID_FOR_AUTHOR, Long.class, authorId);
    }

    @Override
    public Author getAuthorByNewsId(Long newsId) {
        return jdbcTemplate.queryForObject(READ_AUTHOR_BY_NEWS_ID, AuthorDAO.authorRowMapper(), newsId);
    }
}
