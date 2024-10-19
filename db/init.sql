-- Скрипт для создания таблиц в базе данных

CREATE TABLE saved_directory (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    files_count INT DEFAULT 0,
    date_of_add TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dirs_count INT DEFAULT 0,
    files_size INT DEFAULT 0
);

CREATE TABLE header_file (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    size DECIMAL DEFAULT NULL,
    dir_id INT,
    FOREIGN KEY (dir_id) REFERENCES saved_directory(id) ON DELETE CASCADE
);
