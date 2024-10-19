package ru.guryanov.daf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.guryanov.daf.dto.SavedDirectoryDTO;
import ru.guryanov.daf.mapper.HeaderFileMapper;
import ru.guryanov.daf.mapper.SavedDirectoryMapper;
import ru.guryanov.daf.services.HeaderFileService;
import ru.guryanov.daf.services.SavedDirectoryService;

import java.util.List;

@RestController
@RequestMapping("/daf")

public class DAFController {
    private final HeaderFileService headerFileService;
    private final SavedDirectoryService savedDirectoryService;
    private final HeaderFileMapper headerFileMapper;
    private final SavedDirectoryMapper savedDirectoryMapper;

    @Autowired
    public DAFController(HeaderFileService headerFileService, SavedDirectoryService savedDirectoryService, HeaderFileMapper headerFileMapper, SavedDirectoryMapper savedDirectoryMapper) {
        this.headerFileService = headerFileService;
        this.savedDirectoryService = savedDirectoryService;
        this.headerFileMapper = headerFileMapper;
        this.savedDirectoryMapper = savedDirectoryMapper;
    }

    @GetMapping("/savedDirectories")
    public List<SavedDirectoryDTO> getAllSavedDirectories() {
        return savedDirectoryService.getAllSavedDirectories().stream().map(savedDirectoryMapper::toDTO).toList();
    }
}
