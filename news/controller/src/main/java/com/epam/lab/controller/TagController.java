package com.epam.lab.controller;

import com.epam.lab.dto.Saving;
import com.epam.lab.dto.TagDTO;
import com.epam.lab.dto.Updating;
import com.epam.lab.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@Validated
@CrossOrigin(origins = {"http://localhost:3000", "http://epbyminw8147.minsk.epam.com:3000"},
        exposedHeaders = {"tags-amount", "page-number"})
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDTO selectTagById(@PathVariable("id")
                                @Min(value = 1, message = "Tag ID must be greater or equal to 1")
                                        long id) {
        return tagService.selectTag(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDTO> selectAllTags(@RequestParam int pageNumber, @RequestParam int howMany,
                                      HttpServletResponse response) {
        int from = pageNumber * howMany;
        long totalNumber = tagService.count();
        if (from >= totalNumber) {
            if (totalNumber % howMany == 0) {
                from = (int) (totalNumber - howMany);
                pageNumber = (int) totalNumber / howMany - 1;
            } else {
                from = (int) (totalNumber - (totalNumber % howMany));
                pageNumber = (int) totalNumber / howMany;
            }
        }

        response.addHeader("tags-amount", String.valueOf(totalNumber));
        response.addHeader("page-number", String.valueOf(pageNumber));
        return tagService.selectTags(from, howMany);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTag(@PathVariable("id")
                          @Min(value = 1, message = "Tag ID must be greater or equal to 1")
                                  long id) {
        tagService.deleteTag(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TagDTO editTag(@Validated(Updating.class) @RequestBody TagDTO tag) {
        return tagService.updateTag(tag);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO addTag(@Validated(Saving.class) @RequestBody TagDTO tagDTO, HttpServletRequest request,
                         HttpServletResponse response) {
        TagDTO savedTagDTO = tagService.addTag(tagDTO);
        long tagId = savedTagDTO.getId();
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + "/" + tagId;
        response.addHeader("Location", uri);
        return savedTagDTO;
    }
}
