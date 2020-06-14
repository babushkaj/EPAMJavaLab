package com.epam.lab.specification;

import com.epam.lab.exception.IncorrectRequestException;
import com.epam.lab.model.News;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.time.LocalDate;

import static com.epam.lab.specification.SpecificationConstants.*;

public class NewsSearchSpecification implements SearchSpecification<News> {

    private SearchCriteria criteria;

    public NewsSearchSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<News> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        switch (criteria.getKey()) {
            case CREATION_DATE:
            case MODIFICATION_DATE:
                return createPredicateForOneWordCriteriaName(root, criteriaBuilder);
            case AUTHOR_NAME:
            case AUTHOR_SURNAME:
            case TAGS_NAME:
                return createPredicateForTwoWordsCriteriaName(root, criteriaBuilder);
            default:
                throw new IncorrectRequestException(
                        MessageFormat.format(SEARCH_CRITERIA_1_2_3_IS_INCORRECT, criteria.getKey(),
                                criteria.getOperation(), criteria.getValue()));
        }
    }

    private Predicate createPredicateForOneWordCriteriaName(Root<News> root, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            return criteriaBuilder.equal(root.get(criteria.getKey()), LocalDate.parse(criteria.getValue()));
        }
        throw new IncorrectRequestException(
                MessageFormat.format(SEARCH_CRITERIA_1_2_3_IS_INCORRECT, criteria.getKey(),
                        criteria.getOperation(), criteria.getValue()));
    }

    private Predicate createPredicateForTwoWordsCriteriaName(Root<News> root, CriteriaBuilder criteriaBuilder) {
        String[] fieldKeys = criteria.getKey().split(UNDERSCORE);
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            return criteriaBuilder.equal(root.join(fieldKeys[0]).get(fieldKeys[1]), criteria.getValue());
        }
        throw new IncorrectRequestException(
                MessageFormat.format(SEARCH_CRITERIA_1_2_3_IS_INCORRECT, criteria.getKey(),
                        criteria.getOperation(), criteria.getValue()));
    }
}
