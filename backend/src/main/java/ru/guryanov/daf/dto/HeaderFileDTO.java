package ru.guryanov.daf.dto;

import ru.guryanov.daf.models.SavedDirectory;

public class HeaderFileDTO {
    private Long id;
    private String name;
    private long size;

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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
