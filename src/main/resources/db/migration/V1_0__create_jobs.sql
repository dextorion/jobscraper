CREATE TABLE jobs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    linkedin_id BIGINT UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE
);

CREATE TABLE tags (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(64) NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE jobs_tags (
    jobs_id INT NOT NULL,
    tags_id INT NOT NULL,
    PRIMARY KEY (jobs_id, tags_id),
    FOREIGN KEY (jobs_id) REFERENCES jobs(id),
    FOREIGN KEY (tags_id) REFERENCES tags(id)
);