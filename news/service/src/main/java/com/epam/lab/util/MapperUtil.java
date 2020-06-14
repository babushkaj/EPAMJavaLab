package com.epam.lab.util;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.dto.NewsDTO;
import com.epam.lab.dto.TagDTO;
import com.epam.lab.dto.UserDTO;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MapperUtil {

    private ModelMapper modelMapper;

    @Autowired
    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertUserToUserDTO(User user) {
        if (user == null)
            return null;
        else {
            return modelMapper.map(user, UserDTO.class);
        }
    }

    public User convertUserDTOToUser(UserDTO userDTO) {
        if (userDTO == null)
            return null;
        else
            return modelMapper.map(userDTO, User.class);
    }

    public TagDTO convertTagToDTO(Tag tag) {
        if (tag == null)
            return null;
        else
            return modelMapper.map(tag, TagDTO.class);
    }

    public Tag convertTagDTOToEntity(TagDTO tagDTO) {
        if (tagDTO == null)
            return null;
        else
            return modelMapper.map(tagDTO, Tag.class);
    }

    public AuthorDTO convertAuthorToDTO(Author author) {
        if (author == null)
            return null;
        else
            return modelMapper.map(author, AuthorDTO.class);
    }

    public Author convertAuthorDTOToEntity(AuthorDTO authorDTO) {
        if (authorDTO == null)
            return null;
        else
            return modelMapper.map(authorDTO, Author.class);
    }

    public NewsDTO convertNewsToDTO(News news) {
        if (news == null)
            return null;
        else {
            NewsDTO newsDTO = modelMapper.map(news, NewsDTO.class);
            newsDTO.setAuthor(convertAuthorToDTO(news.getAuthor()));
            newsDTO.setTags(convertTagSetToDTO(news.getTags()));
            return newsDTO;
        }
    }

    public News convertNewsDTOToEntity(NewsDTO newsDTO) {
        if (newsDTO == null)
            return null;
        else {
            News news = modelMapper.map(newsDTO, News.class);
            news.setAuthor(convertAuthorDTOToEntity(newsDTO.getAuthor()));
            news.setTags(convertTagsDTOSetEntity(newsDTO.getTags()));
            return news;
        }
    }

    private Set<TagDTO> convertTagSetToDTO(Set<Tag> tags) {
        Set<TagDTO> tagsDTO = new HashSet<>();
        if (tags != null) {
            for (Tag t : tags) {
                tagsDTO.add(modelMapper.map(t, TagDTO.class));
            }
        }
        return tagsDTO;
    }

    private Set<Tag> convertTagsDTOSetEntity(Set<TagDTO> tagsDTO) {
        Set<Tag> tags = new HashSet<>();
        if (tagsDTO != null) {
            for (TagDTO t : tagsDTO) {
                tags.add(modelMapper.map(t, Tag.class));
            }
        }
        return tags;
    }
}
