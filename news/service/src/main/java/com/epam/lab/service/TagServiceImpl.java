package com.epam.lab.service;

import com.epam.lab.dto.TagDTO;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagDAO;
import com.epam.lab.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public TagDTO selectTag(long id) {
        return MapperUtil.fromTagToTagDTO(tagDAO.select(id));
    }

    @Override
    public List<TagDTO> selectAllTags() {
        return tagDAO.selectAll().stream().map(MapperUtil::fromTagToTagDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTag(long id) {
        tagDAO.deleteRelationTagToNews(id);
        tagDAO.delete(id);
    }

    @Override
    @Transactional
    public TagDTO updateTag(TagDTO tagDTO) {
        tagDAO.update(MapperUtil.fromTagDTOToTag(tagDTO));
        return selectTag(tagDTO.getId());
    }

    @Override
    @Transactional
    public TagDTO addTag(TagDTO tagDTO) {
        Optional<Tag> tagOptional = tagDAO.getTagByName(tagDTO.getName());
        if (tagOptional.isPresent()) {
            return MapperUtil.fromTagToTagDTO(tagOptional.get());
        } else {
            long tagId = tagDAO.insert(MapperUtil.fromTagDTOToTag(tagDTO));
            return MapperUtil.fromTagToTagDTO(tagDAO.select(tagId));
        }
    }

}
