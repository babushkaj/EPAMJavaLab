package com.epam.lab.specification;

import static com.epam.lab.repository.DAOConstants.QUOTATION_MARK_AND_SPACE;
import static com.epam.lab.repository.DAOConstants.SPACE_AND_QUOTATION_MARK;

public class AuthorNameSearchSpecification implements SearchSpecification {
    private static final String AND_AUTHOR_NAME = " AND author.name ";

    private String operator;
    private String value;

    public AuthorNameSearchSpecification(String operator, String value) {
        this.operator = operator;
        this.value = value;

    }

    @Override
    public String getSql() {
        return AND_AUTHOR_NAME + operator + SPACE_AND_QUOTATION_MARK + value + QUOTATION_MARK_AND_SPACE;
    }

}
