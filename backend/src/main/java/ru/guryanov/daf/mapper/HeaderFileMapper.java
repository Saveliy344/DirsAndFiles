package ru.guryanov.daf.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.guryanov.daf.dto.HeaderFileDTO;
import ru.guryanov.daf.models.HeaderFile;

@Component
public class HeaderFileMapper implements Mapper<HeaderFile, HeaderFileDTO>{

    @Override
    public HeaderFileDTO toDTO(HeaderFile h) {
        HeaderFileDTO dto = new HeaderFileDTO();
        dto.setName(h.getName());
        dto.setSize(h.getSize());
        dto.setId(h.getId());
        dto.setIsDir(h.getIsDir());
        return dto;
    }

    @Override
    public HeaderFile fromDTO(HeaderFileDTO dto) {
        HeaderFile h = new HeaderFile();
        h.setName(dto.getName());
        h.setSize(dto.getSize());
        h.setId(dto.getId());
        h.setIsDir(dto.getIsDir());
        return h;
    }
}
