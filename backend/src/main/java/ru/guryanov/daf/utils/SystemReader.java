package ru.guryanov.daf.utils;

import org.springframework.stereotype.Component;
import ru.guryanov.daf.models.HeaderFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class SystemReader {
    public List<HeaderFile> getAllFilesInDirectory(String directoryName) {
        List<HeaderFile> files = new ArrayList<>();
        File directory = new File(directoryName);

        if (directory.exists() && directory.isDirectory()) {
            traverseDirectory(directory, files);
        } else throw new RuntimeException("Not found");

        return files;
    }

    private void traverseDirectory(File dir, List<HeaderFile> files) {
        File[] fileList = dir.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                HeaderFile headerFile = new HeaderFile();
                headerFile.setName(file.getName());

                if (file.isDirectory()) {
                    // Если это директория, размер = 0
                    headerFile.setSize(0);
                    headerFile.setIsDir(true);
                    files.add(headerFile);
                } else {
                    headerFile.setSize(file.length());  // Если это файл, заполняем его размер
                    files.add(headerFile);
                }
            }
        }
    }
}
