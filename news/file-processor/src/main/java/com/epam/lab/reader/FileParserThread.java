package com.epam.lab.reader;

import com.epam.lab.dto.NewsDTO;
import com.epam.lab.fakemodel.FNews;
import com.epam.lab.service.NewsService;
import com.epam.lab.util.DirectoryUtil;
import com.epam.lab.util.MapperUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

public class FileParserThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(FileParserThread.class);

    private static final String NOT_EXISTS_ERR = "File {0} does not exist!";
    private static final String INVALID_NEWS_ERR = "There is an invalid news in this file {0}";
    private static final String FILE_IN_PROCESS = "File in process {0}";
    private static final String FILE_MOVING = "This file {0} will be moved to {1} directory!";

    private ObjectMapper objectMapper;
    private File file;
    private String errDirectory;
    private NewsValidator validator;
    private Set<String> uniquePaths;
    private NewsService newsService;
    private MapperUtil mapperUtil;

    public FileParserThread(ObjectMapper objectMapper, File file, String errDirectory, NewsValidator validator,
                            Set<String> uniquePaths, NewsService newsService, MapperUtil mapperUtil) {
        this.objectMapper = objectMapper;
        this.file = file;
        this.errDirectory = errDirectory;
        this.validator = validator;
        this.uniquePaths = uniquePaths;
        this.newsService = newsService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public void run() {
        try {
            if (!file.exists()) {
                logger.error(MessageFormat.format(NOT_EXISTS_ERR, file.getAbsolutePath()));
                return;
            }
            logger.debug(MessageFormat.format(FILE_IN_PROCESS, file.getAbsolutePath()));
            try (RandomAccessFile reader = new RandomAccessFile(file, "rw");
                 FileLock lock = reader.getChannel().lock()) {
                String s = reader.readLine();
                List<FNews> parsedNews = objectMapper.readValue(s, new TypeReference<List<FNews>>() {
                });
                for (FNews n : parsedNews) {
                    if (!validator.isValid(n)) {
                        logger.error(MessageFormat.format(INVALID_NEWS_ERR, file.getAbsolutePath()));
                        throw new ValidationException();
                    }
                }
//                parsedNews.forEach(n -> newsService.addNews(convertFNewsToNewsDTO(n)));
                parsedNews.forEach(n -> logger.info(convertFNewsToNewsDTO(n).toString()));
            }
            Files.delete(file.toPath());
        } catch (JsonParseException | UnrecognizedPropertyException | ValidationException e) {
            logger.error(MessageFormat.format(FILE_MOVING, file.getAbsolutePath(), errDirectory));
            try {
                DirectoryUtil.createDirectoryIfNotExist(errDirectory);
                Path movedFile = Files.createFile(Paths.get(errDirectory + file.getName()));
                Files.move(file.toPath(), movedFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            uniquePaths.remove(file.getAbsolutePath());
        }
    }

    private NewsDTO convertFNewsToNewsDTO(FNews fNews) {
        NewsDTO news = new NewsDTO();
        news.setId(fNews.getId());
        news.setTitle(fNews.getTitle());
        news.setShortText(fNews.getShortText());
        news.setFullText(fNews.getFullText());
        news.setCreationDate(fNews.getCreationDate());
        news.setModificationDate(fNews.getModificationDate());
        news.setAuthor(mapperUtil.convertAuthorToDTO(fNews.getAuthor()));
        news.setTags(mapperUtil.convertTagSetToDTO(fNews.getTags()));
        return news;
    }
}
