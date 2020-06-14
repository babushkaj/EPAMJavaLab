package com.epam.lab.repository;

import com.epam.lab.model.News;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;

import java.util.List;

public interface NewsRepository extends AbstractRepository<News> {
    List<News> find(SearchSpecification<News> searchCriteria, List<SortSpecification<News>> sortSpecifications,
                    int from, int howMany);

    long countWithSpecification(SearchSpecification<News> searchSpecification);

    default boolean sortSpecsNotEmpty(List<SortSpecification<News>> sortSpecifications) {
        return !sortSpecifications.isEmpty();
    }
}
