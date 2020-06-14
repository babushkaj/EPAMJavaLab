package com.epam.lab.writer;

import com.epam.lab.fakemodel.FNews;
import com.epam.lab.fakemodel.InvalidFieldNews;
import com.epam.lab.model.Author;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FileCreationThread extends Thread {

    private static final String FILE_NAME_PREFIX = "news-";
    private static final String FILE_FORMAT = ".json";
    private static final String TITLE_TEMPLATE = "{2}_{0}_{1}";
    private static final String CREATED_MESSAGE = "{0} has created {1} files for {2} ms.";

    private final String folderPath;
    private final int filesInFolder;
    private final int newsInFile;
    private final ObjectMapper objectMapper;
    private final AtomicInteger fileNumber;

    private final Faker faker;

    public FileCreationThread(String folderPath, int filesInFolder, int newsInFile, ObjectMapper objectMapper,
                              AtomicInteger fileNumber, Faker faker) {
        this.folderPath = folderPath;
        this.filesInFolder = filesInFolder;
        this.newsInFile = newsInFile;
        this.objectMapper = objectMapper;
        this.fileNumber = fileNumber;
        this.faker = faker;
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        String titlePart = faker.name().firstName();
        List<String> news = prepareFilesToWrite(titlePart);
        for (String s : news) {
            String newFilePath = folderPath + File.separator + FILE_NAME_PREFIX + titlePart +
                    fileNumber.incrementAndGet() + FILE_FORMAT;
            try (RandomAccessFile writer = new RandomAccessFile(new File(newFilePath), "rw")) {
                FileLock lock = writer.getChannel().lock();
                writer.writeBytes(s);
                lock.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long after = System.currentTimeMillis();
        System.out.println(MessageFormat.format(CREATED_MESSAGE, this.getName(), filesInFolder, after - before));
    }

    private List<String> createValidNews(int quantity, String titlePart) {
        List<String> stringsToWrite = new ArrayList<>();
        try {
            for (int i = 0; i < quantity; i++) {
                List<FNews> newsFiles = new ArrayList<>();
                for (int j = 0; j < newsInFile; j++) {
                    Date updated = faker.date().future(100, TimeUnit.DAYS);
                    newsFiles.add(new FNews(null,
                            MessageFormat.format(TITLE_TEMPLATE, getRandomTitlePart(), j, titlePart),
                            faker.lorem().characters(30, 50),
                            faker.lorem().characters(500, 1000),
                            LocalDate.now(),
                            updated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), getRandomAuthor()));
                }
                stringsToWrite.add(objectMapper.writeValueAsString(newsFiles));
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error has happened during valid news creation.");
        }
        return stringsToWrite;
    }

    private List<String> createInvalidJSONNews(int quantity, String titlePart) {
        List<String> stringsToWrite = new ArrayList<>();
        try {
            for (int i = 0; i < quantity; i++) {
                List<FNews> newsFiles = new ArrayList<>();
                for (int j = 0; j < newsInFile; j++) {
                    Date updated = faker.date().future(100, TimeUnit.DAYS);
                    newsFiles.add(new FNews(null,
                            MessageFormat.format(TITLE_TEMPLATE, getRandomTitlePart(), j, titlePart),
                            faker.lorem().characters(30, 50),
                            faker.lorem().characters(500, 1000),
                            LocalDate.now(),
                            updated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), getRandomAuthor()));
                }
                stringsToWrite.add("INVALID" + objectMapper.writeValueAsString(newsFiles) + "INVALID");
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error has happened during invalid JSON file creation.");
        }
        return stringsToWrite;
    }

    private List<String> createInvalidFieldNameNews(int quantity, String titlePart) {
        List<String> stringsToWrite = new ArrayList<>();
        try {
            for (int i = 0; i < quantity; i++) {
                List newsFiles = new ArrayList<>();
                for (int j = 0; j < newsInFile; j++) {
                    Date updated = faker.date().future(100, TimeUnit.DAYS);
                    if (j == newsInFile / 2) {
                        newsFiles.add(new InvalidFieldNews(null,
                                MessageFormat.format(TITLE_TEMPLATE, getRandomTitlePart(), j, titlePart),
                                faker.lorem().characters(30, 50),
                                faker.lorem().characters(500, 1000),
                                LocalDate.now(),
                                updated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), getRandomAuthor()));
                    } else {
                        newsFiles.add(new FNews(null,
                                MessageFormat.format(TITLE_TEMPLATE, getRandomTitlePart(), j, titlePart),
                                faker.lorem().characters(30, 50),
                                faker.lorem().characters(500, 1000),
                                LocalDate.now(),
                                updated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), getRandomAuthor()));
                    }

                }
                stringsToWrite.add(objectMapper.writeValueAsString(newsFiles));
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error has happened during creation of news with invalid field name.");
        }
        return stringsToWrite;
    }

    private List<String> createInvalidFieldValueNews(int quantity, String titlePart) {
        List<String> stringsToWrite = new ArrayList<>();
        try {
            for (int i = 0; i < quantity; i++) {
                List<FNews> newsFiles = new ArrayList<>();
                for (int j = 0; j < newsInFile; j++) {
                    Date updated = faker.date().future(100, TimeUnit.DAYS);
                    if (j == newsInFile / 2) {
                        newsFiles.add(new FNews(null,
                                MessageFormat.format(TITLE_TEMPLATE, getRandomTitlePart(), j, titlePart),
                                faker.lorem().characters(201),
                                faker.lorem().characters(500, 1000),
                                LocalDate.now(),
                                updated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), getRandomAuthor()));
                    } else {
                        newsFiles.add(new FNews(null,
                                MessageFormat.format(TITLE_TEMPLATE, getRandomTitlePart(), j),
                                faker.lorem().characters(30, 50),
                                faker.lorem().characters(500, 1000),
                                LocalDate.now(),
                                updated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), getRandomAuthor()));
                    }

                }
                stringsToWrite.add(objectMapper.writeValueAsString(newsFiles));
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error has happened during creation of news with invalid field value.");
        }
        return stringsToWrite;
    }


    private List<String> prepareFilesToWrite(String titlePart) {
        int validFilesQuantity = filesInFolder / 20 * 17;
        int invalidJSONFilesQuantity = filesInFolder / 20;
        int invalidNewsFieldNameFilesQuantity = filesInFolder / 20;
        int invalidNewsFieldValueFilesQuantity = filesInFolder / 20;

        List<String> filesToWrite = new ArrayList<>();
        filesToWrite.addAll(createValidNews(validFilesQuantity, titlePart));
        filesToWrite.addAll(createInvalidJSONNews(invalidJSONFilesQuantity, titlePart));
        filesToWrite.addAll(createInvalidFieldNameNews(invalidNewsFieldNameFilesQuantity, titlePart));
        filesToWrite.addAll(createInvalidFieldValueNews(invalidNewsFieldValueFilesQuantity, titlePart));

        return filesToWrite;
    }

    private Author getRandomAuthor() {
        return new Author(null, faker.name().firstName(), faker.name().lastName());
    }

    private String getRandomTitlePart(){
        return faker.lorem().characters(5, 10);
    }

}
