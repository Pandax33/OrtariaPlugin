CREATE TABLE guildes (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(100) NOT NULL,
                         suffixe VARCHAR(50),
                         specialisation VARCHAR(100),
                         niveau INT,
                         rank_guilde INT,
                         experience FLOAT,
                         description TEXT,
                         membres JSON
);
