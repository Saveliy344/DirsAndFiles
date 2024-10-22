DROP TABLE IF EXISTS header_file;
DROP TABLE IF EXISTS saved_directory;
CREATE TABLE saved_directory (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    files_count INT DEFAULT 0,
    date_of_add TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dirs_count INT DEFAULT 0,
    files_size BIGINT DEFAULT 0
);

CREATE TABLE header_file (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    size BIGINT DEFAULT NULL,
    is_dir Bool DEFAULT FALSE,
    dir_id INT,
    FOREIGN KEY (dir_id) REFERENCES saved_directory(id) ON DELETE CASCADE
);