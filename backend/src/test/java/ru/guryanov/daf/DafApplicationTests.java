package ru.guryanov.daf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.guryanov.daf.models.HeaderFile;
import ru.guryanov.daf.models.SavedDirectory;
import ru.guryanov.daf.services.DAFService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
class DafApplicationTests {

    @Autowired
    private DAFService dafService;

    @Test
    void contextLoads() {
    }


    //Проверка на корректность размера файлов, количества папок и файлов в директории, её имени
    @Test
    void testOne() {
        //Перед выполнением теста удаляем директорию test и все содержащиеся в ней файлы
        deleteDirectory("test");
        createDirectory("test");
        createFile("test/hello.html");
        createFile("test/hi.html");
        writeToFile("test/hello.html", "hello world");
        writeToFile("test/hi.html", "hi world");
        createDirectory("test/hello-world");
        SavedDirectory savedDirectory = addDirectoryToDBUsingDAFService("test");
        assert (savedDirectory.getFilesSize() == 19);
        assert (savedDirectory.getDirsCount() == 1);
        assert (savedDirectory.getFilesCount() == 2);
        assert (savedDirectory.getName().equals(getAbsoluteName("test")));
        deleteDirectoryFromDB(savedDirectory);
        deleteDirectory("test");
    }
    //Проверка на правильную сортировку файлов (сначала идут папки, а потом файлы)
    @Test
    void testTwo(){
        deleteDirectory("test");
        createDirectory("test");
        createDirectory("test/test1");
        createFile("test/test2");
        createDirectory("test/test3");
        createFile("test/test4");
        SavedDirectory savedDirectory = addDirectoryToDBUsingDAFService("test");
        List<HeaderFile> headerFileList = dafService.getAllFilesAndDirsByDirId(savedDirectory.getId());
        assert headerFileList.getFirst().getIsDir();
        assert headerFileList.get(1).getIsDir();
        assert !headerFileList.get(2).getIsDir();
        assert !headerFileList.getLast().getIsDir();
        deleteDirectoryFromDB(savedDirectory);
        deleteDirectory("test");
    }

    //Проверка на пустые файлы (суммарный размер должен быть равен 0)
    @Test
    void testThree(){
        deleteDirectory("test");
        createDirectory("test");
        createFile("test/test1");
        createFile("test/test2");
        createFile("test/test3");
        createFile("test/test4");
        SavedDirectory savedDirectory = addDirectoryToDBUsingDAFService("test");
        assert savedDirectory.getFilesSize() == 0;
        assert savedDirectory.getFilesCount() == 4;
        assert savedDirectory.getDirsCount() == 0;
        deleteDirectoryFromDB(savedDirectory);
        deleteDirectory("test");
    }

    //Проверка на правильную сортировку файлов (сортировка файлов по алфавиту)
    @Test
    void testFour(){
        deleteDirectory("test");
        createDirectory("test");
        createFile("test/testa1");
        createFile("test/testA2_2@");
        createFile("test/testa2_1@");
        createFile("test/testa3");
        createFile("test/testA4");
        SavedDirectory savedDirectory = addDirectoryToDBUsingDAFService("test");
        List<HeaderFile> headerFileList = dafService.getAllFilesAndDirsByDirId(savedDirectory.getId());
        assert headerFileList.getFirst().getName().equals("testa1");
        assert headerFileList.get(1).getName().equals("testa2_1@");
        assert headerFileList.get(2).getName().equals("testA2_2@");
        assert headerFileList.get(3).getName().equals("testa3");
        assert headerFileList.get(4).getName().equals("testA4");
        deleteDirectoryFromDB(savedDirectory);
        deleteDirectory("test");
    }

    //Указывается абсолютный путь директории (чтобы можно было передавать только название директории
    String getAbsoluteName(String name) {
        Path projectRoot = Paths.get("").toAbsolutePath();
        return projectRoot.resolve(name).toString();
    }

    SavedDirectory addDirectoryToDBUsingDAFService(String directoryName) {
        return dafService.addDirectory(getAbsoluteName(directoryName));
    }

    void deleteDirectoryFromDB(SavedDirectory savedDirectory){
        dafService.deleteSavedDirectory(savedDirectory);
    }

    void writeToFile(String path, String text) {
        path = getAbsoluteName(path);
        try (FileWriter myWriter = new FileWriter(path)) {
            myWriter.write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Действие - добавить директорию, добавить файл, удалить файл или директорию
    void makeAction(String name, int actionId) {
        String fileOrDirName = getAbsoluteName(name);
        try {
            switch (actionId) {
                case 1:
                    writeToFile(fileOrDirName, "");
                    break;
                case 2:
                    Files.deleteIfExists(Path.of(fileOrDirName));
                    break;
                case 3:
                    Files.createDirectory(Path.of(fileOrDirName));
                    break;
                case 4:
                    deleteDirectoryRecursively(fileOrDirName);
                    break;
            }
        } catch (IOException ignored) {

        }
    }

    void createFile(String fileName) {
        makeAction(fileName, 1);
    }

    void createDirectory(String dirName) {
        makeAction(dirName, 3);
    }

    void deleteFile(String name) {
        makeAction(name, 2);
    }

    void deleteDirectory(String name){
        makeAction(name, 4);
    }

    void deleteDirectoryRecursively(String name) throws IOException {
        Path path = Path.of(name);
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    deleteDirectory(entry.toString());
                }
            }
        }
        Files.delete(path);
    }

}