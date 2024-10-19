package ru.guryanov.daf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.guryanov.daf.models.SavedDirectory;

import java.util.Optional;

public interface SavedDirectoryRepository extends JpaRepository<SavedDirectory, Long> {
    Optional<SavedDirectory> findByName(String name);
}