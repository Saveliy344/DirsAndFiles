package ru.guryanov.daf.models;

import jakarta.persistence.*;

@Entity
@Table(name = "header_file")

public class HeaderFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "size")
    private long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dir_id", nullable = false)
    private SavedDirectory savedDirectory;
    @Column(name = "is_dir")
    private boolean isDir;

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

    public SavedDirectory getSavedDirectory() {
        return savedDirectory;
    }

    public void setSavedDirectory(SavedDirectory savedDirectory) {
        this.savedDirectory = savedDirectory;
    }

    @Override
    public String toString() {
        return "HeaderFile{" +
                "size=" + size +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean getIsDir() {
        return isDir;
    }

    public void setIsDir(boolean isDir) {
        this.isDir = isDir;
    }
}
