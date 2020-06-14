package com.epam.lab.service;

import com.epam.lab.exception.IncorrectRequestException;

public enum OrderValue {

    ASC, DESC;

    private static final String ASC_STRING = "asc";
    private static final String DESC_STRING = "desc";
    private static final String IS_INCORRECT_SORTING_ORDER = "' is incorrect sorting order.";
    private static final String QUOTATION_MARK = "'";

    public static OrderValue getOrderValue(String input) {
        switch (input.trim().toLowerCase()) {
            case ASC_STRING:
                return ASC;
            case DESC_STRING:
                return DESC;
            default:
                throw new IncorrectRequestException(QUOTATION_MARK + input + IS_INCORRECT_SORTING_ORDER);
        }
    }
}
