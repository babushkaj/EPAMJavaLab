package com.epam.lab.configuration;

import com.epam.lab.listener.ContextStopListener;
import com.epam.lab.reader.FileFinder;
import com.epam.lab.reader.FileFinderImpl;
import com.epam.lab.reader.NewsValidator;
import com.epam.lab.reader.NewsValidatorImpl;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
@ComponentScan({"com.epam.lab.reader", "com.epam.lab.listener"})
@PropertySource("classpath:file-processor.properties")
@EnableScheduling
public class FileProcessorConfig {

    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    @DependsOn("validator")
    public NewsValidator newsValidator(Validator validator) {
        return new NewsValidatorImpl(validator);
    }

    @Bean
    public FileFinder fileFinder() {
        return new FileFinderImpl();
    }

}
