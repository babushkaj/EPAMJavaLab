package com.epam.lab.service;

import com.epam.lab.dto.NewsDTO;
import com.epam.lab.specification.SearchCriteria;
import com.epam.lab.specification.SortCriteria;

import java.util.List;

public interface NewsService {
    List<NewsDTO> findNews(List<SearchCriteria> searchCriteria, List<SortCriteria> sortCriteria,
                           int from, int howMany
    );

    Long count(List<SearchCriteria> searchCriteria);

    NewsDTO selectNews(long id);

    void deleteNews(long id);

    NewsDTO updateNews(NewsDTO newsDTO);

    NewsDTO addNews(NewsDTO newsDTO);

}
