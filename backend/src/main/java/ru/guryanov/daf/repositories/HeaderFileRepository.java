package ru.guryanov.daf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.guryanov.daf.models.HeaderFile;

import java.util.List;

public interface HeaderFileRepository extends JpaRepository<HeaderFile, Long> {
    //Передаётся id родительской директории, используется специфическая сортировка
    @Query(value = QueryString.queryString, nativeQuery = true)
    List<HeaderFile> findByCustomQuery(@Param("id") long id);
}

class QueryString{
    static final String queryString = """
            SELECT *
            FROM header_file
            
            --Этот раздел формирует массив символов в виде efew_23_212_21 -> [23, 212, 21] и добавляет его в таблицу
            CROSS JOIN LATERAL (
                SELECT\s
                    array_agg(CAST(num AS INT)) AS numbers
                FROM (
                    SELECT unnest(REGEXP_MATCHES(name, '\\d+', 'g')) AS num
                ) AS extracted_numbers
            ) AS extracted
            
            
            WHERE dir_id = :id
            --Сначала сортировка происходит в зависимости от того, является ли это директорией или файлом
            ORDER BY
                CASE
                    WHEN is_dir THEN 0
                    ELSE 1
                END,
            --Затем - по имени без учёта регистра
                LOWER(name),
            --Затем - по массиву полученных чисел
                extracted.numbers;
            """;
}