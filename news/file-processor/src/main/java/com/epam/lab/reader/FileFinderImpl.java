package com.epam.lab.reader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileFinderImpl implements FileFinder {

    public List<Path> findAllFiles(String path) {
        File directory = new File(path);
        List<Path> resultList = new ArrayList<>();
        if (!Files.exists(directory.toPath())) {
            return resultList;
        }

        File[] fList = directory.listFiles();
        if (fList == null) {
            return resultList;
        }

        for (File file : fList) {
            if (file.isFile()) {
                resultList.add(file.toPath());
            } else if (file.isDirectory()) {
                resultList.addAll(findAllFiles(file.getAbsolutePath()));
            }
        }

        return resultList;
    }
}
