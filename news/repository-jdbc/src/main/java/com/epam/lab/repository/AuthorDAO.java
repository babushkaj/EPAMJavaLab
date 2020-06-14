package com.epam.lab.repository;

import com.epam.lab.model.Author;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface AuthorDAO extends AbstractDAO<Author> {

    static RowMapper<Author> authorRowMapper() {
        return (resultSet, i) -> {
            Author author = new Author();

            author.setId(resultSet.getLong("ID"));
            author.setName(resultSet.getString("NAME"));
            author.setSurname(resultSet.getString("SURNAME"));
            return author;
        };
    }

    void deleteRelationAuthorToNews(long authorId);

    List<Long> getAllNewsIdForAuthor(Long authorId);

    Author getAuthorByNewsId(Long newsId);
}
