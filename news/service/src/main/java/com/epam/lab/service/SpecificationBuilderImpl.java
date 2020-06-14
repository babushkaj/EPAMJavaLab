package com.epam.lab.service;

import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecificationBuilderImpl implements SpecificationBuilder {

    private SearchSpecificationFactory searchSpecificationFactory;
    private SortSpecificationFactory sortSpecificationFactory;

    @Autowired
    public SpecificationBuilderImpl(SearchSpecificationFactory searchSpecificationFactory,
                                    SortSpecificationFactory sortSpecificationFactory) {
        this.searchSpecificationFactory = searchSpecificationFactory;
        this.sortSpecificationFactory = sortSpecificationFactory;
    }

    @Override
    public List<SearchSpecification> buildSearchSpecifications(List<SearchCriteria> criteria) {
        List<SearchSpecification> searchSpecifications = new ArrayList<>();
        if (criteria != null && !criteria.isEmpty()) {
            for (SearchCriteria sc : criteria) {
                searchSpecifications.add(searchSpecificationFactory.create(sc));
            }
        }
        return searchSpecifications;
    }

    @Override
    public List<SortSpecification> buildSortSpecifications(List<SortCriteria> criteria) {
        List<SortSpecification> sortSpecifications = new ArrayList<>();
        if (criteria != null && !criteria.isEmpty()) {
            for (SortCriteria sc : criteria) {
                sortSpecifications.add(sortSpecificationFactory.create(sc));
            }
        }
        return sortSpecifications;
    }
}
