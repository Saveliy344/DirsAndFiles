package ru.guryanov.daf.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SavedDirectory")
public class SavedDirectory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "files_count", nullable = false)
    private int filesCount = 0;

    @Column(name = "date_of_add", nullable = false)
    private LocalDateTime dateOfAdd = LocalDateTime.now();

    @Column(name = "dirs_count", nullable = false)
    private int dirsCount = 0;

    @Column(name = "files_size", nullable = false)
    private int filesSize = 0;

    @OneToMany(mappedBy = "savedDirectory", cascade = CascadeType.ALL)
    private List<HeaderFile> headerFiles = new ArrayList<>();


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

    public int getFilesSize() {
        return filesSize;
    }

    public void setFilesSize(int filesSize) {
        this.filesSize = filesSize;
    }

    public List<HeaderFile> getHeaderFiles() {
        return headerFiles;
    }

    public void setHeaderFiles(List<HeaderFile> headerFiles) {
        this.headerFiles = headerFiles;
    }

    public int getDirsCount() {
        return dirsCount;
    }

    public void setDirsCount(int dirsCount) {
        this.dirsCount = dirsCount;
    }
}