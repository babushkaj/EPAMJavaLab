package com.epam.lab.reader;

import com.epam.lab.service.NewsService;
import com.epam.lab.util.DirectoryUtil;
import com.epam.lab.util.MapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@DependsOn({"objectMapper", "newsRepositoryImpl", "newsValidator"})
public class ReaderServiceImpl implements ReaderService {

    private static final Logger logger = LoggerFactory.getLogger(ReaderServiceImpl.class);

    private static final String ERR_MSG = "Error in ReaderService.";
    private static final String EMPTY_QUEUE = "There is no new files. Queue is empty!";

    @Value("${path.root}")
    private String rootDirectory;

    @Value("${path.err}")
    private String errDirectory;

    @Value("${threads.number}")
    private int threadsNumber;

    private ObjectMapper objectMapper;
    private FileFinder fileFinder;
    private NewsValidator newsValidator;
    private NewsService newsService;
    private MapperUtil mapperUtil;

    private ConcurrentLinkedQueue<Path> paths = new ConcurrentLinkedQueue<>();
    private Set<String> uniquePaths = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private ExecutorService executorService;

    @Autowired
    public ReaderServiceImpl(ObjectMapper objectMapper, FileFinder fileFinder,
                             NewsValidator newsValidator, NewsService newsService, MapperUtil mapperUtil) {
        this.objectMapper = objectMapper;
        this.fileFinder = fileFinder;
        this.newsValidator = newsValidator;
        this.newsService = newsService;
        this.mapperUtil = mapperUtil;
    }

    @PostConstruct
    public void postConstruct() {
        executorService = Executors.newFixedThreadPool(threadsNumber);
    }

    @Scheduled(fixedRate = 100)
    public void startReading() {
        try {
            DirectoryUtil.createDirectoryIfNotExist(errDirectory);
            List<Path> newPaths = fileFinder.findAllFiles(rootDirectory);
            addUniquePathsToQueue(newPaths);
            parseFiles(newsValidator, executorService);
        } catch (IOException e) {
            logger.error(ERR_MSG, e);
        }
    }

    private void addUniquePathsToQueue(List<Path> newPaths) {
        for (Path p : newPaths) {
            if (!uniquePaths.contains(p.toFile().getAbsolutePath())) {
                paths.add(p);
                uniquePaths.add(p.toFile().getAbsolutePath());
            }
        }
    }

    private void parseFiles(NewsValidator newsValidator, ExecutorService executorService) {
        if (paths.isEmpty()) {
            logger.debug(EMPTY_QUEUE);
            return;
        }
        while (paths.peek() != null) {
            executorService.submit(new FileParserThread(objectMapper, paths.poll().toFile(), errDirectory,
                    newsValidator, uniquePaths, newsService, mapperUtil));
        }
    }

    public void shutdownExecutor() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }


}
