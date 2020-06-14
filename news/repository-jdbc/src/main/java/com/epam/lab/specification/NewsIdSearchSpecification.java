package com.epam.lab.specification;

import static com.epam.lab.repository.DAOConstants.SPACE;

public class NewsIdSearchSpecification implements SearchSpecification {
    private static final String AND_NEWS_ID = " AND news.id ";

    private String operator;
    private String value;

    public NewsIdSearchSpecification(String operator, String value) {
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String getSql() {
        return AND_NEWS_ID + operator + SPACE + value + SPACE;
    }
}
