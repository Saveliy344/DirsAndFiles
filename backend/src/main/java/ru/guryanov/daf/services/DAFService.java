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


    //Если успешно добавилась, вернётся true, иначе - false
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addDirectory(String directoryName) {
        System.out.println(BASE);
        //Для добавления относительных путей
        if (directoryName.startsWith(".")){
            directoryName = BASE + "/" + directoryName;
        }
        String CorrectPath = null;
        try {
            CorrectPath = systemReader.getCorrectPathToDirectory(directoryName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Если в таблице уже есть директория, то удаляем её, все связанные записи удалятся автоматически из-за каскадирования
        Optional<SavedDirectory> previousDirectory = savedDirectoryRepository.findByName(CorrectPath);
        previousDirectory.ifPresent(this::deleteDirectory);

        List<HeaderFile> headerFiles = systemReader.getAllFilesInDirectory(CorrectPath);
        int dirsCount = 0;
        int filesCount = 0;
        long sumFilesSize = 0;
        SavedDirectory savedDirectory = new SavedDirectory();
        savedDirectory.setName(CorrectPath);
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
    }

}
