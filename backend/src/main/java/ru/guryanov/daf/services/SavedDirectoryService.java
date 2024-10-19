package ru.guryanov.daf.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.guryanov.daf.models.SavedDirectory;
import ru.guryanov.daf.repositories.SavedDirectoryRepository;
import ru.guryanov.daf.utils.SystemReader;

import java.util.List;

@Service
@Transactional
public class SavedDirectoryService {
    private final SavedDirectoryRepository savedDirectoryRepository;
    private final SystemReader systemReader;

    @Autowired
    public SavedDirectoryService(SavedDirectoryRepository savedDirectoryRepository, SystemReader systemReader) {
        this.savedDirectoryRepository = savedDirectoryRepository;
        this.systemReader = systemReader;
    }

    public List<SavedDirectory> getAllSavedDirectories(){
        systemReader.getAllFilesInDirectory("/home");
        return savedDirectoryRepository.findAll();
    }
}
