package com.epam.lab.service;

import com.epam.lab.dto.TagDTO;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final MapperUtil mapperUtil;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, MapperUtil mapperUtil) {
        this.tagRepository = tagRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public TagDTO selectTag(long id) {
        return mapperUtil.convertTagToDTO(tagRepository.findById(id));
    }

    @Override
    public List<TagDTO> selectTags(int from, int howMany) {
        return tagRepository.findAll(from, howMany).stream().map(mapperUtil::convertTagToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteTag(long id) {
        tagRepository.delete(id);
    }

    @Override
    public TagDTO updateTag(TagDTO tagDTO) {
        tagRepository.update(mapperUtil.convertTagDTOToEntity(tagDTO));
        return selectTag(tagDTO.getId());
    }

    @Override
    public TagDTO addTag(TagDTO tagDTO) {
        TagDTO tag = mapperUtil.convertTagToDTO(tagRepository.findByName(tagDTO.getName()));
        long tagId;
        if (tag == null) {
            tagId = tagRepository.insert(mapperUtil.convertTagDTOToEntity(tagDTO));
        } else {
            tagId = tag.getId();
        }
        return mapperUtil.convertTagToDTO(tagRepository.findById(tagId));
    }

    @Override
    public Long count() {
        return tagRepository.countAll();
    }

}
