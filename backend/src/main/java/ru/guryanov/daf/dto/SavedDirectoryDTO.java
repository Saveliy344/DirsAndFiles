package ru.guryanov.daf.dto;

import ru.guryanov.daf.models.HeaderFile;

import java.time.LocalDateTime;
import java.util.List;

public class SavedDirectoryDTO {
    private Long id;
    private String name;
    private int filesCount;
    private LocalDateTime dateOfAdd;
    private int dirsCount;
    private long filesSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }

    public LocalDateTime getDateOfAdd() {
        return dateOfAdd;
    }

    public void setDateOfAdd(LocalDateTime dateOfAdd) {
        this.dateOfAdd = dateOfAdd;
    }


    public long getFilesSize() {
        return filesSize;
    }

    public void setFilesSize(long filesSize) {
        this.filesSize = filesSize;
    }

    public int getDirsCount() {
        return dirsCount;
    }

    public void setDirsCount(int dirsCount) {
        this.dirsCount = dirsCount;
    }
}
