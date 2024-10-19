package ru.guryanov.daf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.guryanov.daf.repositories.HeaderFileRepository;

@Service

public class HeaderFileService {
    private final HeaderFileRepository headerFileRepository;
    @Autowired
    public HeaderFileService(HeaderFileRepository headerFileRepository) {
        this.headerFileRepository = headerFileRepository;
    }
}
