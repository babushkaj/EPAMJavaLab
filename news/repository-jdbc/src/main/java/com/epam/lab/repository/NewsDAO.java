package com.epam.lab.repository;

import com.epam.lab.model.News;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface NewsDAO extends AbstractDAO<News> {

    static RowMapper<News> newsRowMapper() {
        return (resultSet, i) -> {
            News news = new News();

            news.setId(resultSet.getLong("ID"));
            news.setTitle(resultSet.getString("TITLE"));
            news.setShortText(resultSet.getString("SHORT_TEXT"));
            news.setFullText(resultSet.getString("FULL_TEXT"));
            news.setCreationDate(resultSet.getDate("CREATION_DATE").toLocalDate());
            news.setModificationDate(resultSet.getDate("MODIFICATION_DATE").toLocalDate());
            return news;
        };
    }

    void setRelationNewsToAuthor(long newsId, long authorId);

    void setRelationNewsToTag(long newsId, long tagId);

    void deleteRelationNewsToAuthor(long newsId);

    void deleteRelationNewsToAllTags(long newsId);

    List<News> find(List<SearchSpecification> searchSpecifications, List<SortSpecification> sortSpecifications);

    Long getNewsCount();


}
