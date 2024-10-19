package ru.guryanov.daf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.guryanov.daf.models.HeaderFile;

public interface HeaderFileRepository extends JpaRepository<HeaderFile, Long> {
}