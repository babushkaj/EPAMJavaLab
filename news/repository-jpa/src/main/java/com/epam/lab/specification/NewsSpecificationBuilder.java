package com.epam.lab.specification;

import com.epam.lab.model.News;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewsSpecificationBuilder {
    private final List<SearchCriteria> params;

    public NewsSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public NewsSpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public SearchSpecification<News> build() {
        if (params.isEmpty()) {
            return null;
        }
        List<SearchSpecification<News>> specs = params.stream()
                .map(NewsSearchSpecification::new)
                .collect(Collectors.toList());

        SearchSpecification<News> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = result.and(specs.get(i));
        }
        return result;
    }
}
