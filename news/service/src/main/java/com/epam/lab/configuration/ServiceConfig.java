package com.epam.lab.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(RepositoryConfig.class)
@Configuration
@ComponentScan("com.epam.lab")
@EnableTransactionManagement
public class ServiceConfig {

}
