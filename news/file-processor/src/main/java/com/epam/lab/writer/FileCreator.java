package com.epam.lab.writer;

public interface FileCreator {
    void createFiles(int filesInFolder, int newsInFile, int depth, int totalFolderCount,
                     int testTimeMs, int periodTimeMs) throws InterruptedException ;
}
