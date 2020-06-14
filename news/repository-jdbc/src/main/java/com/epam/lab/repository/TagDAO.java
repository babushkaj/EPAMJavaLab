package com.epam.lab.repository;

import com.epam.lab.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public interface TagDAO extends AbstractDAO<Tag> {

    static RowMapper<Tag> tagRowMapper() {
        return (resultSet, i) -> {
            Tag tag = new Tag();

            tag.setId(resultSet.getLong("ID"));
            tag.setName(resultSet.getString("NAME"));
            return tag;
        };
    }

    Optional<Tag> getTagByName(String tagName);

    void deleteRelationTagToNews(long tagId);

    List<Tag> getAllTagsByNewsId(Long newsId);


}
