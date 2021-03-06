package com.epam.lab.service;

import com.epam.lab.dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO selectTag(long id);

    List<TagDTO> selectTags(int from, int howMany);

    void deleteTag(long id);

    TagDTO updateTag(TagDTO tag);

    TagDTO addTag(TagDTO tag);

    Long count();

}
