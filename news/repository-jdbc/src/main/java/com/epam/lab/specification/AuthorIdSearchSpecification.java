package com.epam.lab.specification;

import com.epam.lab.repository.DAOConstants;

public class AuthorIdSearchSpecification implements SearchSpecification {

    private static final String AND_AUTHOR_ID = " AND author.id ";

    private String operator;
    private String value;

    public AuthorIdSearchSpecification(String operator, String value) {
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String getSql() {
        return AND_AUTHOR_ID + operator + DAOConstants.SPACE + value;
    }

}
