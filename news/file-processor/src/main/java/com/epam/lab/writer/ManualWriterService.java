package com.epam.lab.writer;

import com.epam.lab.util.DirectoryUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class ManualWriterService {

    public static final String PROPERTIES = "file-processor.properties";

    static {
        ClassLoader classLoader = ManualWriterService.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES);
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ROOT_DIRECTORY = prop.getProperty("path.root");
        ERR_DIRECTORY = prop.getProperty("path.err");
        TEST_TIME_MS = Integer.parseInt(prop.getProperty("test.time"));
        PERIOD_TIME_MS = Integer.parseInt(prop.getProperty("period.time"));
        SUBFOLDERS_COUNT = Integer.parseInt(prop.getProperty("subfolder.count"));
        FILES_COUNT = Integer.parseInt(prop.getProperty("files.count"));
        NEWS_IN_FILE = Integer.parseInt(prop.getProperty("news.in.file"));

    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String ROOT_DIRECTORY;
    public static final String ERR_DIRECTORY;

    private static final int TEST_TIME_MS;
    private static final int PERIOD_TIME_MS;
    private static final int SUBFOLDERS_COUNT;
    private static final int FILES_COUNT;
    private static final int NEWS_IN_FILE;

    public static void main(String[] args) throws IOException, InterruptedException {

        FolderCreator folderCreator = new FolderCreatorImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));

        DirectoryUtil.deleteDirectory(ROOT_DIRECTORY);
        DirectoryUtil.deleteDirectory(ERR_DIRECTORY);

        Faker faker = new Faker();
        FileCreator fileCreator = new FileCreatorImpl(folderCreator, objectMapper, ROOT_DIRECTORY, faker);

        fileCreator.createFiles(FILES_COUNT, NEWS_IN_FILE, SUBFOLDERS_COUNT / 3,
                SUBFOLDERS_COUNT + 1, TEST_TIME_MS, PERIOD_TIME_MS);

    }

}

