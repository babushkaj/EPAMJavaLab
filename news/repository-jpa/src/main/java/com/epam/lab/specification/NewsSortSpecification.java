package com.epam.lab.specification;

import com.epam.lab.exception.IncorrectRequestException;
import com.epam.lab.model.News;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;

import static com.epam.lab.specification.SpecificationConstants.*;

public class NewsSortSpecification implements SortSpecification<News> {

    private SortCriteria sortCriteria;

    public NewsSortSpecification(SortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    @Override
    public Order toOrder(Root<News> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        switch (sortCriteria.getParam()) {
            case CREATION_DATE:
            case MODIFICATION_DATE:
                return getOrderForNewsDate(root, criteriaBuilder);
            case AUTHOR_NAME:
            case AUTHOR_SURNAME:
                return getOrderForAuthorName(root, criteriaBuilder);
            default:
                throw new IncorrectRequestException(MessageFormat.format(SORT_CRITERIA_1_2_IS_INCORRECT,
                        sortCriteria.getParam(), sortCriteria.getOrderValue()));
        }
    }

    private Order getOrderForNewsDate(Root<News> root, CriteriaBuilder criteriaBuilder) {
        return sortCriteria.getOrderValue() == OrderValue.DESC ?
                criteriaBuilder.desc(root.get(sortCriteria.getParam())) :
                criteriaBuilder.asc(root.get(sortCriteria.getParam()));
    }

    private Order getOrderForAuthorName(Root<News> root, CriteriaBuilder criteriaBuilder) {
        String[] fieldKeys = sortCriteria.getParam().split(UNDERSCORE);
        return sortCriteria.getOrderValue() == OrderValue.DESC ?
                criteriaBuilder.desc(root.join(fieldKeys[0]).get(fieldKeys[1])) :
                criteriaBuilder.asc(root.join(fieldKeys[0]).get(fieldKeys[1]));
    }

}
