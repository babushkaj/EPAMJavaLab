package com.epam.lab.controller;

import com.epam.lab.dto.TagDTO;
import com.epam.lab.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDTO selectTagById(@PathVariable("id") long id) {
        return tagService.selectTag(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDTO> selectAllTags() {
        return tagService.selectAllTags();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTag(@PathVariable("id") long id) {
        tagService.deleteTag(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TagDTO editTag(@Valid @RequestBody TagDTO tag) {
        return tagService.updateTag(tag);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO addTag(@Valid @RequestBody TagDTO tagDTO, HttpServletRequest request,
                         HttpServletResponse response) {
        TagDTO savedTagDTO = tagService.addTag(tagDTO);
        long tagId = savedTagDTO.getId();
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + "/" + tagId;
        response.addHeader("Location", uri);
        return savedTagDTO;
    }
}
