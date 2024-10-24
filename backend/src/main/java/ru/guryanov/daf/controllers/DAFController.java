package ru.guryanov.daf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.guryanov.daf.dto.HeaderFileDTO;
import ru.guryanov.daf.dto.SavedDirectoryDTO;
import ru.guryanov.daf.mapper.HeaderFileMapper;
import ru.guryanov.daf.mapper.SavedDirectoryMapper;
import ru.guryanov.daf.services.DAFService;

import java.util.List;

@RestController
@RequestMapping("/daf")

public class DAFController {
    private final DAFService dafService;
    private final HeaderFileMapper headerFileMapper;
    private final SavedDirectoryMapper savedDirectoryMapper;

    @Autowired
    public DAFController(DAFService dafService, HeaderFileMapper headerFileMapper, SavedDirectoryMapper savedDirectoryMapper) {
        this.dafService = dafService;
        this.headerFileMapper = headerFileMapper;
        this.savedDirectoryMapper = savedDirectoryMapper;
    }

    @GetMapping("/directories")
    public List<SavedDirectoryDTO> getAllSavedDirectories() {
        return dafService.getAllSavedDirectories(false).stream().map(savedDirectoryMapper::toDTO).toList();
    }

    @GetMapping("/directories/asc")
    public List<SavedDirectoryDTO> getAllSavedDirectoriesOrderByDateASC() {
        return dafService.getAllSavedDirectories(true).stream().map(savedDirectoryMapper::toDTO).toList();
    }

    @GetMapping("/filesAndDirs/{id}")
    public List<HeaderFileDTO> getFiles(@PathVariable("id") Long id) {
        return dafService.getAllFilesAndDirsByDirId(id).stream().map(headerFileMapper::toDTO).toList();
    }

    @PostMapping("/addDirectory")
    public ResponseEntity<String> addDirectory(@RequestBody DirectoryRequest request) {
        try {
            dafService.addDirectory(request.getName());
            return ResponseEntity.ok().build();
        }
        catch (CannotAcquireLockException e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Таблица изменяется другой транзакцией!");
        }
        catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

class DirectoryRequest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
