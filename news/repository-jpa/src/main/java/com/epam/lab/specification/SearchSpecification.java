package com.epam.lab.specification;

import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface SearchSpecification<T> {

    @Nullable
    default SearchSpecification<T> and(@Nullable SearchSpecification<T> other) {
        return SpecificationComposition.composed(this, other, CriteriaBuilder::and);
    }

    @Nullable
    Predicate toPredicate(Root<T> var1, CriteriaQuery<?> var2, CriteriaBuilder var3);

}
