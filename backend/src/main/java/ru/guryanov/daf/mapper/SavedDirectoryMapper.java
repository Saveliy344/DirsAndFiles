package ru.guryanov.daf.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.guryanov.daf.dto.SavedDirectoryDTO;
import ru.guryanov.daf.models.SavedDirectory;

@Component

public class SavedDirectoryMapper implements Mapper<SavedDirectory, SavedDirectoryDTO>{

    @Override
    public SavedDirectoryDTO toDTO(SavedDirectory s) {
        SavedDirectoryDTO dto = new SavedDirectoryDTO();
        dto.setFilesCount(s.getFilesCount());
        dto.setDateOfAdd(s.getDateOfAdd());
        dto.setFilesSize(s.getFilesSize());
        dto.setId(s.getId());
        dto.setDirsCount(s.getDirsCount());
        dto.setName(s.getName());
        return dto;
    }

    @Override
    public SavedDirectory fromDTO(SavedDirectoryDTO dto) {
        SavedDirectory s = new SavedDirectory();
        s.setFilesCount(dto.getFilesCount());
        s.setDateOfAdd(dto.getDateOfAdd());
        s.setFilesSize(dto.getFilesSize());
        s.setId(dto.getId());
        s.setDirsCount(dto.getDirsCount());
        s.setName(dto.getName());
        return s;
    }
}
