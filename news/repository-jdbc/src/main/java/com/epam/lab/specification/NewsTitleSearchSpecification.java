package com.epam.lab.specification;

import static com.epam.lab.repository.DAOConstants.QUOTATION_MARK_AND_SPACE;
import static com.epam.lab.repository.DAOConstants.SPACE_AND_QUOTATION_MARK;

public class NewsTitleSearchSpecification implements SearchSpecification {
    private static final String AND_NEWS_TITLE = " AND news.title ";

    private String operator;
    private String value;

    public NewsTitleSearchSpecification(String operator, String value) {
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String getSql() {
        return AND_NEWS_TITLE + value + SPACE_AND_QUOTATION_MARK + operator + QUOTATION_MARK_AND_SPACE;
    }
}
