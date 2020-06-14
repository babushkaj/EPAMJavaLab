package com.epam.lab.reader;

import java.nio.file.Path;
import java.util.List;

public interface FileFinder {
    List<Path> findAllFiles(String path);
}
