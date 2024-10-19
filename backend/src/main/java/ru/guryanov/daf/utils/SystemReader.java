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
            System.out.println(files);
        } else {
            System.out.println("Директория не существует или не является директорией");
        }

        return files;
    }

    private void traverseDirectory(File dir, List<HeaderFile> files) {
        File[] fileList = dir.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                HeaderFile headerFile = new HeaderFile();
                headerFile.setName(file.getName());

                if (file.isDirectory()) {
                    headerFile.setSize(-1.0);  // Если это директория, размер = -1
                    files.add(headerFile);
                } else {
                    headerFile.setSize((double) file.length());  // Если это файл, заполняем его размер
                    files.add(headerFile);
                }
            }
        }
    }
}
