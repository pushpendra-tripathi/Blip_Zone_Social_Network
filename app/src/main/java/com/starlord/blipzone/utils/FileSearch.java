package com.starlord.blipzone.utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {

    /**
     * Search a directory and return a list of all **directories** contained inside
     */
    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        for (File listFile : listFiles) {
            if (listFile.isDirectory()) {
                pathArray.add(listFile.getAbsolutePath());
            }
        }
        return pathArray;
    }

    /**
     * Search a directory and return a list of all **files** contained inside
     */
    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        for (File listFile : listFiles) {
            if (listFile.isFile()) {
                pathArray.add(listFile.getAbsolutePath());
            }
        }
        return pathArray;
    }
}
