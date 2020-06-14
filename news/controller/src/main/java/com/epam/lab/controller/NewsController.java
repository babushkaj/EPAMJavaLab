package com.epam.lab.controller;

import com.epam.lab.dto.NewsDTO;
import com.epam.lab.dto.Saving;
import com.epam.lab.dto.Updating;
import com.epam.lab.service.NewsService;
import com.epam.lab.specification.OrderValue;
import com.epam.lab.specification.SearchCriteria;
import com.epam.lab.specification.SortCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/news")
@Validated
@CrossOrigin(origins = {"http://localhost:3000", "http://epbyminw8147.minsk.epam.com:3000"},
        exposedHeaders = {"news-amount", "page-number", "search-string"})
public class NewsController {

    private static final String PARSE_SEARCH_CRITERIA_REGEX = "(\\w+?)(:)(\\w+?)(,+?)";
    private static final String PARSE_SORT_CRITERIA_REGEX = "(\\w+?)(:)(\\w+.?),+?";

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsDTO selectNewsById(@PathVariable("id")
                                  @Min(value = 1, message = "News ID must be greater or equal to 1")
                                          Long id) {
        return newsService.selectNews(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsDTO addNews(@Validated(Saving.class) @RequestBody NewsDTO news, HttpServletRequest request,
                           HttpServletResponse response) {
        NewsDTO savedNewsDTO = newsService.addNews(news);
        long newsId = savedNewsDTO.getId();
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + request.getServletPath() + "/" + newsId;
        response.addHeader("Location", uri);
        return savedNewsDTO;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNews(@PathVariable("id")
                           @Min(value = 1, message = "News ID must be greater or equal to 1")
                                   long id) {
        newsService.deleteNews(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsDTO editNews(@Validated(Updating.class) @RequestBody NewsDTO news) {
        return newsService.updateNews(news);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDTO> findNews(@RequestParam(value = "search", required = false) String searchString,
                                  @RequestParam(value = "sort", required = false) String sortString,
                                  @RequestParam int pageNumber, @RequestParam int howMany,
                                  HttpServletResponse response) {

        List<SearchCriteria> searchCriteria = parseSearchCriteria(searchString);
        List<SortCriteria> sortCriteria = parseSortCriteria(sortString);

        int from = pageNumber * howMany;
        long totalNumber = newsService.count(searchCriteria);
        if (from >= totalNumber) {
            if (totalNumber % howMany == 0) {
                from = (int) (totalNumber - howMany);
                pageNumber = (int) totalNumber / howMany - 1;
            } else {
                from = (int) (totalNumber - (totalNumber % howMany));
                pageNumber = (int) totalNumber / howMany;
            }
        }

        response.addHeader("news-amount", String.valueOf(totalNumber));
        response.addHeader("page-number", String.valueOf(pageNumber));
        response.addHeader("search-string", searchString);

        return newsService.findNews(searchCriteria, sortCriteria, from, howMany);
    }

    private List<SearchCriteria> parseSearchCriteria(String searchString) {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (searchString != null && !searchString.isEmpty()) {
            Pattern pattern = Pattern.compile(PARSE_SEARCH_CRITERIA_REGEX);
            Matcher matcher = pattern.matcher(searchString + ",");
            while (matcher.find()) {
                searchCriteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }

        return searchCriteriaList;
    }

    private List<SortCriteria> parseSortCriteria(String sortString) {
        List<SortCriteria> sortCriteriaList = new ArrayList<>();
        if (sortString != null && !sortString.isEmpty()) {
            Pattern pattern = Pattern.compile(PARSE_SORT_CRITERIA_REGEX);
            Matcher matcher = pattern.matcher(sortString + ",");
            while (matcher.find()) {
                sortCriteriaList.add(new SortCriteria(matcher.group(1), OrderValue.getOrderValue(matcher.group(3))));
            }
        }

        return sortCriteriaList;
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public Long getNewsCount() {
        return newsService.count(new ArrayList<>());
    }

}
