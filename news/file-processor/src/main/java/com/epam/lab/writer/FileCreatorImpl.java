package com.epam.lab.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FileCreatorImpl implements FileCreator {
    private FolderCreator folderCreator;
    private ObjectMapper objectMapper;
    private String rootDirectory;
    private Faker faker;

    public FileCreatorImpl(FolderCreator folderCreator, ObjectMapper objectMapper, String rootDirectory, Faker faker) {
        this.folderCreator = folderCreator;
        this.objectMapper = objectMapper;
        this.rootDirectory = rootDirectory;
        this.faker = faker;
    }

    @Override
    public void createFiles(int filesInFolder, int newsInFile, int depth,
                            int totalFolderCount, int testTimeMs, int periodTimeMs) throws InterruptedException {
        List<String> folders = folderCreator.createFolders(rootDirectory, depth, totalFolderCount);
        AtomicInteger fileNumber = new AtomicInteger();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(folders.size());
        for (String f : folders) {
            executorService.scheduleWithFixedDelay(
                    new FileCreationThread(f, filesInFolder, newsInFile, objectMapper, fileNumber, faker),
                    0, periodTimeMs, TimeUnit.MILLISECONDS);
        }
        Thread.sleep(testTimeMs);
        executorService.shutdown();
    }
}
