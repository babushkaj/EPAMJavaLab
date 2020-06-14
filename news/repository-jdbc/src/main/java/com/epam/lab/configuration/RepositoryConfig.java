package com.epam.lab.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("com.epam.lab")
@PropertySource("classpath:database.properties")
public class RepositoryConfig {

    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource() {
        final String URL = "url";
        final String DRIVER = "driver";
        final String USER = "dbuser";
        final String PASSWORD = "dbpassword";
        final String POOL_SIZE = "poolsize";

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty(DRIVER));
        hikariConfig.setJdbcUrl(environment.getProperty(URL));
        hikariConfig.setUsername(environment.getProperty(USER));
        hikariConfig.setPassword(environment.getProperty(PASSWORD));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(environment.getProperty(POOL_SIZE))));

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
