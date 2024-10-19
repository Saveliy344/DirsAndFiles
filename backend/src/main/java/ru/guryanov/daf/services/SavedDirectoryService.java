package ru.guryanov.daf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.guryanov.daf.models.SavedDirectory;
import ru.guryanov.daf.repositories.SavedDirectoryRepository;

import java.util.List;

@Service
public class SavedDirectoryService {
    private final SavedDirectoryRepository savedDirectoryRepository;

    @Autowired
    public SavedDirectoryService(SavedDirectoryRepository savedDirectoryRepository) {
        this.savedDirectoryRepository = savedDirectoryRepository;
    }

    public List<SavedDirectory> getAllSavedDirectories(){
        return savedDirectoryRepository.findAll();
    }
}
