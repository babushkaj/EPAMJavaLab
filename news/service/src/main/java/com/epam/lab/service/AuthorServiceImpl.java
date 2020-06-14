package com.epam.lab.service;

import com.epam.lab.dto.AuthorDTO;
import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final MapperUtil mapperUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, MapperUtil mapperUtil) {
        this.authorRepository = authorRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public AuthorDTO selectAuthor(long id) {
        return mapperUtil.convertAuthorToDTO(authorRepository.findById(id));
    }

    @Override
    public List<AuthorDTO> selectAllAuthors() {
        return authorRepository.findAll().stream().map(mapperUtil::convertAuthorToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteAuthor(long id) {
        authorRepository.delete(id);
    }

    @Override
    public AuthorDTO updateAuthor(AuthorDTO authorDTO) {
        authorRepository.update(mapperUtil.convertAuthorDTOToEntity(authorDTO));
        return selectAuthor(authorDTO.getId());
    }

    @Override
    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        long authorId = authorRepository.insert(mapperUtil.convertAuthorDTOToEntity(authorDTO));
        return mapperUtil.convertAuthorToDTO(authorRepository.findById(authorId));
    }

}
