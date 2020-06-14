package com.epam.lab.service;

import com.epam.lab.exception.IncorrectRequestException;
import com.epam.lab.specification.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
class SearchSpecificationFactory {
    private static final String NEWS_ID = "news_id";
    private static final String TITLE = "title";
    private static final String AUTHOR_ID = "author_id";
    private static final String AUTHOR_NAME = "author_name";
    private static final String AUTHOR_SURNAME = "author_surname";
    private static final String TAG = "tag";

    SearchSpecification create(SearchCriteria sc) {

        switch (sc.getKey().trim().toLowerCase()) {
            case NEWS_ID: {
                return new NewsIdSearchSpecification(defineOperator(sc.getOperation()), sc.getValue());
            }
            case TITLE: {
                return new NewsTitleSearchSpecification(defineOperator(sc.getOperation()), sc.getValue());
            }
            case AUTHOR_ID: {
                return new AuthorIdSearchSpecification(defineOperator(sc.getOperation()), sc.getValue());
            }
            case AUTHOR_NAME: {
                return new AuthorNameSearchSpecification(defineOperator(sc.getOperation()), sc.getValue());
            }
            case AUTHOR_SURNAME: {
                return new AuthorSurnameSearchSpecification(defineOperator(sc.getOperation()), sc.getValue());
            }
            case TAG: {
                String tagNames = sc.getValue();
                List<String> tagNameList = Arrays.asList(tagNames.split(";"));
                return new TagsSearchSpecification(tagNameList);
            }
            default:
                throw new IncorrectRequestException("Search criteria with key = '" + sc.getKey() + "' and value = '"
                        + sc.getValue() + "' is incorrect!");
        }
    }


    private String defineOperator(SearchOperation searchOperation) {
        switch (searchOperation) {
            case EQUALITY: {
                return "=";
            }
            case NEGATION: {
                return "!=";
            }
            default:
                throw new IncorrectRequestException("Search operation '" + searchOperation + "' is incorrect!");
        }
    }
}
