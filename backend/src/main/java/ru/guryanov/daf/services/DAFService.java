package ru.guryanov.daf.services;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.guryanov.daf.models.HeaderFile;
import ru.guryanov.daf.models.SavedDirectory;
import ru.guryanov.daf.repositories.SavedDirectoryRepository;
import ru.guryanov.daf.utils.SystemReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DAFService {
    private final SavedDirectoryRepository savedDirectoryRepository;
    private final SystemReader systemReader;

    @Autowired
    public DAFService(SavedDirectoryRepository savedDirectoryRepository, SystemReader systemReader) {
        this.savedDirectoryRepository = savedDirectoryRepository;
        this.systemReader = systemReader;
    }

    public List<SavedDirectory> getAllSavedDirectories() {
        return savedDirectoryRepository.findAll();
    }

    private void deleteDirectory(SavedDirectory savedDirectory) {
        savedDirectoryRepository.delete(savedDirectory);
    }

    //Вернуть список файлов и директорий по id родительской директории
    public List<HeaderFile> getAllFilesAndDirsByDirId(Long id) {
        SavedDirectory savedDirectory = savedDirectoryRepository.findById(id).orElse(null);
        //Если не найдена директория с таким id, возвращаем null
        if (savedDirectory == null) return new ArrayList<>();
        return savedDirectory.getHeaderFiles();
    }


    //Если успешно добавилась, вернётся true, иначе - false
    public boolean addDirectory(String directoryName) {
        try {
            //Если в таблице уже есть директория, то удаляем, все связанные записи удалятся автоматически из-за каскадирования
            Optional<SavedDirectory> previousDirectory = savedDirectoryRepository.findByName(directoryName);
            previousDirectory.ifPresent(this::deleteDirectory);

            List<HeaderFile> headerFiles = systemReader.getAllFilesInDirectory(directoryName);
            int dirsCount = 0;
            int filesCount = 0;
            long sumFilesSize = 0;
            SavedDirectory savedDirectory = new SavedDirectory();
            savedDirectory.setName(directoryName);
            for (HeaderFile headerFile : headerFiles) {
                //Если это директория, то её размер равен нулю
                if (headerFile.getSize() == 0) dirsCount++;
                else filesCount++;
                sumFilesSize += headerFile.getSize();
                headerFile.setSavedDirectory(savedDirectory);
                savedDirectory.addHeaderFile(headerFile);
            }
            savedDirectory.setDirsCount(dirsCount);
            savedDirectory.setFilesSize(sumFilesSize);
            savedDirectory.setFilesCount(filesCount);
            savedDirectoryRepository.save(savedDirectory);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}