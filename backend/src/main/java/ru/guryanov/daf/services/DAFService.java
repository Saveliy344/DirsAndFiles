package ru.guryanov.daf.services;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import ru.guryanov.daf.models.HeaderFile;
import ru.guryanov.daf.models.SavedDirectory;
import ru.guryanov.daf.repositories.HeaderFileRepository;
import ru.guryanov.daf.repositories.SavedDirectoryRepository;
import ru.guryanov.daf.utils.SystemReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DAFService {
    private final SavedDirectoryRepository savedDirectoryRepository;
    private final HeaderFileRepository headerFileRepository;
    private final SystemReader systemReader;

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
    public boolean addDirectory(String directoryName) {
        //Обрезка лишних символов-разделителей
        if (directoryName.length() != 1 && directoryName.endsWith("/")){
            //Заменить все символы "/" в конце на "", то есть строка "/hello///" заменится на "/hello"
            directoryName = directoryName.replaceAll("/+$", "");
        }
        try {
            //Если в таблице уже есть директория, то удаляем её, все связанные записи удалятся автоматически из-за каскадирования
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
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
