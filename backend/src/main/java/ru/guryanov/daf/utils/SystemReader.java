package ru.guryanov.daf.utils;

import org.springframework.stereotype.Component;
import ru.guryanov.daf.models.HeaderFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SystemReader {
    public List<HeaderFile> getAllFilesInDirectory(String directoryName) {
        List<HeaderFile> files = new ArrayList<>();
        File file = new File(directoryName);
        if (file.exists() && file.isDirectory()) {
            traverseDirectory(file, files);
        } else throw new RuntimeException("Not found");

        return files;
    }

    /*Нужно, чтобы директория добавлялась в таблицу в нормальном виде
    Например, если указать имя директории как /opt//////directory//
    То добавится /opt/directory
     */
    public String getCorrectPathToDirectory(String directoryName) throws IOException {
        File file = new File(directoryName);
        if (!file.exists()) throw new RuntimeException("Not found");
        return file.getCanonicalPath();
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
                    // Если это файл, заполняем его размер
                    headerFile.setSize(file.length());
                    files.add(headerFile);
                }
            }
        }
    }
}
