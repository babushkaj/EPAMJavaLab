package com.epam.lab.writer;

import java.util.List;

public interface FolderCreator {
    List<String> createFolders(String rootFolderPath, int depth, int totalFolderCount);
}
