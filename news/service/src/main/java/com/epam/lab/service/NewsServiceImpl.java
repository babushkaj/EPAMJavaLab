package com.epam.lab.service;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.dto.NewsDTO;
import com.epam.lab.dto.TagDTO;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.AuthorDAO;
import com.epam.lab.repository.NewsDAO;
import com.epam.lab.repository.TagDAO;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;
import com.epam.lab.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsDAO newsDAO;
    private final TagDAO tagDAO;
    private final AuthorDAO authorDAO;
    private final SpecificationBuilder specificationBuilder;

    @Autowired
    public NewsServiceImpl(NewsDAO newsDAO, TagDAO tagDAO, AuthorDAO authorDAO,
                           SpecificationBuilder specificationBuilder) {
        this.newsDAO = newsDAO;
        this.tagDAO = tagDAO;
        this.authorDAO = authorDAO;
        this.specificationBuilder = specificationBuilder;
    }

    @Override
    public NewsDTO selectNews(long id) {
        NewsDTO newsDTO = MapperUtil.fromNewsToNewsDTO(newsDAO.select(id));
        setAuthorToNews(newsDTO);
        setTagsToNews(newsDTO);
        return newsDTO;
    }

    private void setAuthorToNews(NewsDTO newsDTO) {
        AuthorDTO authorDTO = MapperUtil.fromAuthorToAuthorDTO(authorDAO.getAuthorByNewsId(newsDTO.getId()));
        newsDTO.setAuthor(authorDTO);
    }

    private void setTagsToNews(NewsDTO newsDTO) {
        Set<TagDTO> tags = getTagDTOForNews(newsDTO);
        if (isTagsNotEmpty(tags)) {
            for (TagDTO tag : tags) {
                newsDTO.getTags().add(tag);
            }
        }
    }

    private Set<TagDTO> getTagDTOForNews(NewsDTO newsDTO) {
        return tagDAO.getAllTagsByNewsId(newsDTO.getId()).stream()
                .map(MapperUtil::fromTagToTagDTO)
                .collect(Collectors.toSet());
    }

    private boolean isTagsNotEmpty(Set<TagDTO> tags) {
        return !tags.isEmpty();
    }

    @Override
    @Transactional
    public void deleteNews(long id) {
        newsDAO.deleteRelationNewsToAuthor(id);
        newsDAO.deleteRelationNewsToAllTags(id);
        newsDAO.delete(id);
    }

    @Override
    @Transactional
    public NewsDTO updateNews(NewsDTO newsDTO) {
        AuthorDTO authorDTO = newsDTO.getAuthor();
        authorDAO.update(MapperUtil.fromAuthorDTOToAuthor(authorDTO));
        updateRelationNewsAuthor(newsDTO.getId(), authorDTO.getId());

        newsDAO.deleteRelationNewsToAllTags(newsDTO.getId());
        Set<TagDTO> tags = newsDTO.getTags();
        if (tags != null && isTagsNotEmpty(tags)) {
            for (TagDTO tag : tags) {
                saveOrUpdateTag(newsDTO.getId(), tag);
            }
        }
        newsDTO.setModificationDate(LocalDate.now());
        newsDAO.update(MapperUtil.fromNewsDTOToNews(newsDTO));
        return selectNews(newsDTO.getId());
    }

    private void updateRelationNewsAuthor(long newsId, long authorId) {
        newsDAO.deleteRelationNewsToAuthor(newsId);
        newsDAO.setRelationNewsToAuthor(newsId, authorId);
    }

    private void saveOrUpdateTag(long newsId, TagDTO tag) {
        Optional<Tag> tagOptional = tagDAO.getTagByName(tag.getName());
        if (tagOptional.isPresent()) {
            newsDAO.setRelationNewsToTag(newsId, tagOptional.get().getId());
        } else {
            long tagId = tagDAO.insert(MapperUtil.fromTagDTOToTag(tag));
            newsDAO.setRelationNewsToTag(newsId, tagId);
        }
    }

    @Override
    @Transactional
    public NewsDTO addNews(NewsDTO newsDTO) {
        newsDTO.setCreationDate(LocalDate.now());
        newsDTO.setModificationDate(newsDTO.getCreationDate());
        long newsId = newsDAO.insert(MapperUtil.fromNewsDTOToNews(newsDTO));

        AuthorDTO authorDTO = newsDTO.getAuthor();
        Long authorId = authorDTO.getId();
        if (authorId == null) {
            authorId = authorDAO.insert(MapperUtil.fromAuthorDTOToAuthor(authorDTO));
            authorDTO.setId(authorId);
        }
        addRelationNewsAuthor(newsId, authorId);

        Set<TagDTO> tagDTOs = newsDTO.getTags();
        if (tagDTOs != null && !tagDTOs.isEmpty()) {
            for (TagDTO tag : tagDTOs) {
                saveOrUpdateTag(newsId, tag);
            }
        }

        return selectNews(newsId);
    }

    private void addRelationNewsAuthor(long newsId, long authorId) {
        newsDAO.setRelationNewsToAuthor(newsId, authorId);
    }

    @Override
    public List<NewsDTO> findNews(List<SearchCriteria> searchCriteria, List<SortCriteria> sortCriteria) {

        List<SearchSpecification> searchSpecifications = specificationBuilder.buildSearchSpecifications(searchCriteria);
        List<SortSpecification> sortSpecifications = specificationBuilder.buildSortSpecifications(sortCriteria);
        List<NewsDTO> newsList = getNewsDTOBySpecifications(searchSpecifications, sortSpecifications);
        List<NewsDTO> uniqueNews = chooseUniqueNews(newsList);

        for (NewsDTO news : uniqueNews) {
            setAuthorToNews(news);
            setTagsToNews(news);
        }

        return uniqueNews;
    }

    private List<NewsDTO> getNewsDTOBySpecifications(List<SearchSpecification> searchSpecifications,
                                                     List<SortSpecification> sortSpecifications) {
        return newsDAO.find(searchSpecifications, sortSpecifications).stream().
                map(MapperUtil::fromNewsToNewsDTO).
                collect(Collectors.toList());
    }

    private List<NewsDTO> chooseUniqueNews(List<NewsDTO> newsList) {
        List<NewsDTO> uniqueNews = new ArrayList<>();
        Set<Long> newsId = new HashSet<>();
        for (NewsDTO news : newsList) {
            long id = news.getId();
            if (!newsId.contains(id)) {
                uniqueNews.add(news);
                newsId.add(id);
            }
        }
        return uniqueNews;
    }

    @Override
    public Long getNewsCount() {
        return newsDAO.getNewsCount();
    }
}
