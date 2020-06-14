package com.epam.lab.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Comparator;

public class DirectoryUtil {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryUtil.class);

    private static final String DELETING_ERR = "Error to delete folder {0}";

    public static void deleteDirectory(String directoryPath) throws IOException {
        if (Files.exists(Paths.get(directoryPath))) {
            Files.walk(Paths.get(directoryPath))
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            logger.error(MessageFormat.format(DELETING_ERR, directoryPath), e);
                        }
                    });
        }
    }

    public static void createDirectoryIfNotExist(String directoryPath) throws IOException {
        if (!Files.exists(Paths.get(directoryPath))) {
            Files.createDirectory(Paths.get(directoryPath));
        }
    }
}
