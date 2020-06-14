package com.epam.lab.util;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.dto.NewsDTO;
import com.epam.lab.dto.TagDTO;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import org.springframework.beans.BeanUtils;

public class MapperUtil {
    public static Author fromAuthorDTOToAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        BeanUtils.copyProperties(authorDTO, author);
        return author;
    }

    public static AuthorDTO fromAuthorToAuthorDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        BeanUtils.copyProperties(author, authorDTO);
        return authorDTO;
    }

    public static Tag fromTagDTOToTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagDTO, tag);
        return tag;
    }

    public static TagDTO fromTagToTagDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        BeanUtils.copyProperties(tag, tagDTO);
        return tagDTO;
    }

    public static News fromNewsDTOToNews(NewsDTO newsDTO) {
        News news = new News();
        BeanUtils.copyProperties(newsDTO, news);
        return news;
    }

    public static NewsDTO fromNewsToNewsDTO(News news) {
        NewsDTO newsDTO = new NewsDTO();
        BeanUtils.copyProperties(news, newsDTO);
        return newsDTO;
    }

}
