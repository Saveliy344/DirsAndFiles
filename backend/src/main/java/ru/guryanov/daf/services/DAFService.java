package ru.guryanov.daf.services;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import ru.guryanov.daf.models.HeaderFile;
import ru.guryanov.daf.models.SavedDirectory;
import ru.guryanov.daf.repositories.HeaderFileRepository;
import ru.guryanov.daf.repositories.SavedDirectoryRepository;
import ru.guryanov.daf.utils.SystemReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DAFService {
    private final SavedDirectoryRepository savedDirectoryRepository;
    private final HeaderFileRepository headerFileRepository;
    private final SystemReader systemReader;
    @Value("${base-directory}")
    private String BASE;
    @Value("${home-directory}")
    private String HOME;

    @Autowired
    public DAFService(SavedDirectoryRepository savedDirectoryRepository, HeaderFileRepository headerFileRepository, SystemReader systemReader) {
        this.savedDirectoryRepository = savedDirectoryRepository;
        this.headerFileRepository = headerFileRepository;
        this.systemReader = systemReader;
    }

    public List<SavedDirectory> getAllSavedDirectories(boolean orderByDateASC) {
        if (orderByDateASC)
            return savedDirectoryRepository.findAllByOrderByDateOfAddAsc();
        else
            return savedDirectoryRepository.findAllByOrderByDateOfAddDesc();
    }

    private void deleteDirectory(SavedDirectory savedDirectory) {
        savedDirectoryRepository.delete(savedDirectory);
    }

    //Вернуть список файлов и директорий по id родительской директории
    public List<HeaderFile> getAllFilesAndDirsByDirId(Long id) {
        return headerFileRepository.findByCustomQuery(id);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public SavedDirectory addDirectory(String directoryName) {
        //Для добавления домашней директории
        if (directoryName.equals("~")){
            directoryName = HOME;
        }
        //Для добавления относительных путей
        if (directoryName.startsWith(".")){
            directoryName = BASE + "/" + directoryName;
        }
        String correctPath = null;
        try {
            correctPath = systemReader.getCorrectPathToDirectory(directoryName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Если в таблице уже есть директория, то удаляем её, все связанные записи удалятся автоматически из-за каскадирования
        Optional<SavedDirectory> previousDirectory = savedDirectoryRepository.findByName(correctPath);
        previousDirectory.ifPresent(this::deleteDirectory);

        List<HeaderFile> headerFiles = systemReader.getAllFilesInDirectory(correctPath);
        int dirsCount = 0;
        int filesCount = 0;
        long sumFilesSize = 0;
        SavedDirectory savedDirectory = new SavedDirectory();
        savedDirectory.setName(correctPath);
        for (HeaderFile headerFile : headerFiles) {
            //Если это директория, то её размер равен нулю
            if (headerFile.getIsDir()) dirsCount++;
            else filesCount++;
            sumFilesSize += headerFile.getSize();
            headerFile.setSavedDirectory(savedDirectory);
            savedDirectory.addHeaderFile(headerFile);
        }
        savedDirectory.setDirsCount(dirsCount);
        savedDirectory.setFilesSize(sumFilesSize);
        savedDirectory.setFilesCount(filesCount);
        savedDirectoryRepository.save(savedDirectory);
        return savedDirectory;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteSavedDirectory(SavedDirectory savedDirectory){
        savedDirectoryRepository.delete(savedDirectory);
    }

}
