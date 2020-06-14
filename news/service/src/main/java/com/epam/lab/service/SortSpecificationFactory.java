package com.epam.lab.service;

import com.epam.lab.exception.IncorrectRequestException;
import com.epam.lab.specification.SortSpecification;
import com.epam.lab.specification.SortSpecificationImpl;
import org.springframework.stereotype.Component;

@Component
class SortSpecificationFactory {

    private static final String CREATION_DATE = "creation_date";
    private static final String MODIFICATION_DATE = "modification_date";
    private static final String AUTHOR_NAME = "author_name";
    private static final String AUTHOR_SURNAME = "author_surname";
    private static final String NEWS = "news.";
    private static final String AUTHOR = "author.";

    SortSpecification create(SortCriteria sc) {

        switch (sc.getParam().trim().toLowerCase()) {
            case CREATION_DATE: {
                return new SortSpecificationImpl(NEWS + CREATION_DATE, sc.getOrderValue().toString());
            }
            case MODIFICATION_DATE: {
                return new SortSpecificationImpl(NEWS + MODIFICATION_DATE, sc.getOrderValue().toString());
            }
            case AUTHOR_NAME: {
                return new SortSpecificationImpl(AUTHOR + AUTHOR_NAME, sc.getOrderValue().toString());
            }
            case AUTHOR_SURNAME: {
                return new SortSpecificationImpl(AUTHOR + AUTHOR_SURNAME, sc.getOrderValue().toString());
            }
            default:
                throw new IncorrectRequestException("Sort criteria with param = '" + sc.getParam() + "' and order = '" +
                        sc.getOrderValue() + "' is incorrect!");
        }
    }
}
