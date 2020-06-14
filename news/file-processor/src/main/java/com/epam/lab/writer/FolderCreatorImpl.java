package com.epam.lab.writer;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FolderCreatorImpl implements FolderCreator{

    private final Logger logger = Logger.getLogger(FolderCreatorImpl.class);

    private static final String FOLDER_PATH_TEMPLATE = "{0}" + File.separator + "{1}{2}";

    @Override
    public List<String> createFolders(String rootFolderPath, int depth, int totalFolderCount) {
        List<String> folderPaths = new ArrayList<>();

        try {
            Path rootPath = Paths.get(rootFolderPath);

            folderPaths.add(Files.createDirectory(rootPath).toString());

            int total = 1;

            while (total < totalFolderCount) {
                String parentFolder = rootFolderPath;
                for (int i = 1; i <= depth && total < totalFolderCount; i++) {
                    Path dir = Files.createDirectory(Paths.get(MessageFormat.format(FOLDER_PATH_TEMPLATE, parentFolder, i, total)));
                    folderPaths.add(dir.toString());
                    parentFolder = dir.toString();
                    total++;
                }
            }

        } catch (IOException e) {
            logger.error("Error has occured during folder creation.", e);
        }

        return folderPaths;
    }
}
