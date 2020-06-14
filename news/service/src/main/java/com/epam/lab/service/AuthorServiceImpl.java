package com.epam.lab.service;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.repository.AuthorDAO;
import com.epam.lab.repository.NewsDAO;
import com.epam.lab.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDAO authorDAO;
    private final NewsDAO newsDAO;

    @Autowired
    public AuthorServiceImpl(AuthorDAO authorDAO, NewsDAO newsDAO) {
        this.authorDAO = authorDAO;
        this.newsDAO = newsDAO;
    }

    @Override
    public AuthorDTO selectAuthor(long id) {
        return MapperUtil.fromAuthorToAuthorDTO(authorDAO.select(id));
    }

    @Override
    public List<AuthorDTO> selectAllAuthors() {
        return authorDAO.selectAll().stream().map(MapperUtil::fromAuthorToAuthorDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAuthor(long id) {
        List<Long> allAuthorNewsId = authorDAO.getAllNewsIdForAuthor(id);
        authorDAO.deleteRelationAuthorToNews(id);
        for (Long newsId : allAuthorNewsId) {
            newsDAO.deleteRelationNewsToAllTags(newsId);
            newsDAO.delete(newsId);
        }
        authorDAO.delete(id);
    }

    @Override
    @Transactional
    public AuthorDTO updateAuthor(AuthorDTO authorDTO) {
        authorDAO.update(MapperUtil.fromAuthorDTOToAuthor(authorDTO));
        return selectAuthor(authorDTO.getId());
    }

    @Override
    @Transactional
    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        long authorId = authorDAO.insert(MapperUtil.fromAuthorDTOToAuthor(authorDTO));
        return MapperUtil.fromAuthorToAuthorDTO(authorDAO.select(authorId));
    }

}
