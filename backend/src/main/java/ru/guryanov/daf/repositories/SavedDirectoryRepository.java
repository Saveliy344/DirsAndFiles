package ru.guryanov.daf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.guryanov.daf.models.SavedDirectory;

public interface SavedDirectoryRepository extends JpaRepository<SavedDirectory, Long> {
}