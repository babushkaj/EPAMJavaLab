package com.epam.lab.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("com.epam.lab.repository")
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
public class RepositoryConfig {

    private static final String CREATION_TABLES_SQL_SCRIPT_NAME = "tables.sql";
    private static final String FILL_TABLES_SQL_SCRIPT_NAME = "data.sql";

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.epam.lab.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaDialect(new HibernateJpaDialect());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    @Profile("prod")
    public DataSource dataSource(Environment environment) {
        final String URL = "url";
        final String DRIVER = "driver";
        final String USER = "db.user";
        final String PASS = "db.password";
        final String POOL_SIZE = "pool.size";

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty(DRIVER));
        hikariConfig.setJdbcUrl(environment.getProperty(URL));
        hikariConfig.setUsername(environment.getProperty(USER));
        hikariConfig.setPassword(environment.getProperty(PASS));
        hikariConfig.setMaximumPoolSize(Integer.valueOf(environment.getProperty(POOL_SIZE)));

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    @Profile("test")
    public EmbeddedDatabase embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .addScript(CREATION_TABLES_SQL_SCRIPT_NAME)
                .addScript(FILL_TABLES_SQL_SCRIPT_NAME)
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");

        return properties;
    }

}
