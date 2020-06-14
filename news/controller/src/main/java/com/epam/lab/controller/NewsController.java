package com.epam.lab.controller;

import com.epam.lab.dto.NewsDTO;
import com.epam.lab.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/news")
public class NewsController {

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsDTO selectUserById(@PathVariable("id") Long id) {
        return newsService.selectNews(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNews(@PathVariable("id") long id) {
        newsService.deleteNews(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsDTO editNews(@Valid @RequestBody NewsDTO news) {
        return newsService.updateNews(news);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDTO> findNews(@RequestParam(value = "search", required = false) String searchString,
                                  @RequestParam(value = "sort", required = false) String sortString) {

        List<SearchCriteria> searchCriteria = parseSearchCriteria(searchString);
        List<SortCriteria> sortCriteria = parseSortCriteria(sortString);

        return newsService.findNews(searchCriteria, sortCriteria);
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public Long getNewsCount() {
        return newsService.getNewsCount();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsDTO addTag(@Valid @RequestBody NewsDTO news, HttpServletRequest request,
                          HttpServletResponse response) {
        NewsDTO savedNewsDTO = newsService.addNews(news);
        long newsId = savedNewsDTO.getId();
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + "/" + newsId;
        response.addHeader("Location", uri);
        return savedNewsDTO;
    }

    private List<SearchCriteria> parseSearchCriteria(String searchString) {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (searchString != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|!)(\\w+.+?),");
            Matcher matcher = pattern.matcher(searchString + ",");
            while (matcher.find()) {
                searchCriteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(3),
                        SearchOperation.getSimpleOperation(matcher.group(2).charAt(0))));
            }
        }

        return searchCriteriaList;
    }

    private List<SortCriteria> parseSortCriteria(String sortString) {
        List<SortCriteria> sortCriteriaList = new ArrayList<>();
        if (sortString != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(\\w+.+?),");
            Matcher matcher = pattern.matcher(sortString + ",");
            while (matcher.find()) {
                sortCriteriaList.add(new SortCriteria(matcher.group(1), OrderValue.getOrderValue(matcher.group(3))));
            }
        }

        return sortCriteriaList;
    }
}
