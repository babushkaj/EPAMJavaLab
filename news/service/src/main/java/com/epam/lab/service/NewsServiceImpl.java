package com.epam.lab.service;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.dto.NewsDTO;
import com.epam.lab.dto.TagDTO;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.specification.NewsSortSpecification;
import com.epam.lab.specification.NewsSpecificationBuilder;
import com.epam.lab.specification.SearchCriteria;
import com.epam.lab.specification.SortCriteria;
import com.epam.lab.specification.SortSpecification;
import com.epam.lab.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final MapperUtil mapperUtil;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, AuthorRepository authorRepository,
                           TagRepository tagRepository, MapperUtil mapperUtil) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public NewsDTO selectNews(long id) {
        return mapperUtil.convertNewsToDTO(newsRepository.findById(id));
    }

    @Override
    public void deleteNews(long id) {
        newsRepository.delete(id);
    }

    @Override
    public NewsDTO updateNews(NewsDTO newsDTO) {
        saveAuthorIfNotExist(newsDTO.getAuthor());

        Set<TagDTO> tags = newsDTO.getTags();
        for (TagDTO t : tags) {
            saveTagIfNotExist(t);
        }

        newsDTO.setModificationDate(LocalDate.now());
        newsRepository.update(mapperUtil.convertNewsDTOToEntity(newsDTO));
        return selectNews(newsDTO.getId());
    }

    @Override
    public NewsDTO addNews(NewsDTO newsDTO) {
        saveAuthorIfNotExist(newsDTO.getAuthor());

        Set<TagDTO> tags = newsDTO.getTags();
        for (TagDTO t : tags) {
            saveTagIfNotExist(t);
        }

        newsDTO.setCreationDate(LocalDate.now());
        newsDTO.setModificationDate(newsDTO.getCreationDate());
        long newsId = newsRepository.insert(mapperUtil.convertNewsDTOToEntity(newsDTO));
        newsDTO.setId(newsId);
        return newsDTO;
    }

    private void saveAuthorIfNotExist(AuthorDTO author) {
        if (author.getId() == null) {
            long authorId = authorRepository.insert(mapperUtil.convertAuthorDTOToEntity(author));
            author.setId(authorId);
        }
    }

    private void saveTagIfNotExist(TagDTO t) {
        Tag tag = tagRepository.findByName(t.getName());
        if (tag == null) {
            long tagId = tagRepository.insert(mapperUtil.convertTagDTOToEntity(t));
            t.setId(tagId);
        } else {
            t.setId(tag.getId());
        }
    }

    @Override
    public List<NewsDTO> findNews(List<SearchCriteria> searchCriteria, List<SortCriteria> sortCriteria, int from, int howMany) {
        if (searchCriteria.isEmpty()) {
            return getAllNews(from, howMany);
        }

        NewsSpecificationBuilder newsSpecificationBuilder = new NewsSpecificationBuilder();
        for (SearchCriteria sc : searchCriteria) {
            newsSpecificationBuilder.with(sc);
        }
        List<SortSpecification<News>> sortSpecifications = new ArrayList<>();
        if (sortCriteriaNotEmpty(sortCriteria)) {
            for (SortCriteria sc : sortCriteria) {
                sortSpecifications.add(new NewsSortSpecification(sc));
            }
        }

        return newsRepository.find(newsSpecificationBuilder.build(), sortSpecifications, from, howMany)
                .stream()
                .map(mapperUtil::convertNewsToDTO)
                .collect(Collectors.toList());
    }

    private List<NewsDTO> getAllNews(int from, int howMany) {
        return newsRepository.findAll(from, howMany)
                .stream()
                .map(mapperUtil::convertNewsToDTO)
                .collect(Collectors.toList());
    }

    private boolean sortCriteriaNotEmpty(List<SortCriteria> sortCriteria) {
        return !sortCriteria.isEmpty();
    }

    @Override
    public Long count(List<SearchCriteria> searchCriteria) {
        if(searchCriteria.isEmpty())
        return newsRepository.countAll();
        else {
            NewsSpecificationBuilder newsSpecificationBuilder = new NewsSpecificationBuilder();
            for (SearchCriteria sc : searchCriteria) {
                newsSpecificationBuilder.with(sc);
            }
            return newsRepository.countWithSpecification(newsSpecificationBuilder.build());
        }
    }

}
