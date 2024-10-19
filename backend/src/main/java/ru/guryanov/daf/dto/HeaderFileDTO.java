package ru.guryanov.daf.dto;

import ru.guryanov.daf.models.SavedDirectory;

public class HeaderFileDTO {
    private Long id;
    private String name;
    private Double size;

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

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
