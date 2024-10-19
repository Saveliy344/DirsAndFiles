package ru.guryanov.daf.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.guryanov.daf.repositories.HeaderFileRepository;

@Service
@Transactional

public class HeaderFileService {
    private final HeaderFileRepository headerFileRepository;
    @Autowired
    public HeaderFileService(HeaderFileRepository headerFileRepository) {
        this.headerFileRepository = headerFileRepository;
    }
}
