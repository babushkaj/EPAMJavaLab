package com.epam.lab.repository;

import com.epam.lab.model.Tag;

public interface TagRepository extends AbstractRepository<Tag> {
    Tag findByName(String tagName);
}
