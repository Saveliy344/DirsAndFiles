package ru.guryanov.daf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.guryanov.daf.services.HeaderFileService;
import ru.guryanov.daf.services.SavedDirectoryService;

@RestController
@RequestMapping("/daf")

public class DAFController {
    private final HeaderFileService headerFileService;
    private final SavedDirectoryService savedDirectoryService;
    @Autowired

    public DAFController(HeaderFileService headerFileService, SavedDirectoryService savedDirectoryService) {
        this.headerFileService = headerFileService;
        this.savedDirectoryService = savedDirectoryService;
    }
}
