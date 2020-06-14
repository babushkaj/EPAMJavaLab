package com.epam.lab.reader;

import com.epam.lab.fakemodel.FNews;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;

public class NewsValidatorImpl implements NewsValidator {

    private final Validator validator;

    @Autowired
    public NewsValidatorImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean isValid(FNews news) {
        return validator.validate(news).isEmpty();
    }
}
