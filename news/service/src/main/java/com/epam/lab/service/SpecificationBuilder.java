package com.epam.lab.service;

import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;

import java.util.List;

public interface SpecificationBuilder {
    List<SearchSpecification> buildSearchSpecifications(List<SearchCriteria> criteriaList);

    List<SortSpecification> buildSortSpecifications(List<SortCriteria> criteriaList);
}
