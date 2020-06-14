package com.epam.lab.specification;

import static com.epam.lab.repository.DAOConstants.*;

public class AuthorSurnameSearchSpecification implements SearchSpecification {
    private static final String AND_AUTHOR_SURNAME = " AND author.surname ";

    private String operator;
    private String value;

    public AuthorSurnameSearchSpecification(String operator, String value) {
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String getSql() {
        return AND_AUTHOR_SURNAME + operator + SPACE_AND_QUOTATION_MARK + value + QUOTATION_MARK_AND_SPACE;
    }

}
