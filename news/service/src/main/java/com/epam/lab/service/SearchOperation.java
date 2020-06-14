package com.epam.lab.service;

import com.epam.lab.exception.IncorrectRequestException;

public enum SearchOperation {

    EQUALITY, NEGATION;

    private static final String IS_INCORRECT_SORTING_ORDER = "' is incorrect search operation.";
    private static final String QUOTATION_MARK = "'";

    public static SearchOperation getSimpleOperation(final char input) {
        switch (input) {
            case ':':
                return EQUALITY;
            case '!':
                return NEGATION;
            default:
                throw new IncorrectRequestException(QUOTATION_MARK + input + IS_INCORRECT_SORTING_ORDER);
        }
    }


}
