package com.epam.lab.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "news-rest-api";

    private static final String API_NEWS_PATH = "/api/news";
    private static final String API_TAGS_PATH = "/api/tags";
    private static final String API_AUTHORS_PATH = "/api/authors";
    private static final String API_USERS_PATH = "/api/authors";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, API_USERS_PATH + "/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, API_USERS_PATH).permitAll()
                .antMatchers(HttpMethod.DELETE, API_USERS_PATH + "/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, API_NEWS_PATH + "/**").permitAll()
                .antMatchers(HttpMethod.GET, API_TAGS_PATH + "/**").permitAll()
                .antMatchers(HttpMethod.GET, API_AUTHORS_PATH + "/**").hasRole("USER")
                .antMatchers(HttpMethod.POST).hasRole("USER")
                .antMatchers(HttpMethod.PUT).hasRole("USER")
                .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                .anyRequest().authenticated();
    }
}
