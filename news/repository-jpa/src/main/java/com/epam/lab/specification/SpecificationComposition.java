package com.epam.lab.specification;

import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public class SpecificationComposition {

    SpecificationComposition() {
    }

    @Nullable
    static <T> SearchSpecification<T> composed(@Nullable SearchSpecification<T> lhs,
                                               @Nullable SearchSpecification<T> rhs,
                                               SpecificationComposition.Combiner combiner) {
        return (root, query, builder) -> {
            Predicate otherPredicate = toPredicate(lhs, root, query, builder);
            Predicate thisPredicate = toPredicate(rhs, root, query, builder);
            if (thisPredicate == null) {
                return otherPredicate;
            } else {
                return otherPredicate == null ? thisPredicate : combiner.combine(builder, thisPredicate, otherPredicate);
            }
        };
    }

    private static <T> Predicate toPredicate(SearchSpecification<T> specification, Root<T> root,
                                             CriteriaQuery<?> query, CriteriaBuilder builder) {
        return specification == null ? null : specification.toPredicate(root, query, builder);
    }

    interface Combiner extends Serializable {
        Predicate combine(CriteriaBuilder var1, @Nullable Predicate var2, @Nullable Predicate var3);
    }

}
