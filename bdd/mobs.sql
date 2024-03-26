CREATE TABLE Mobs (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      zoneApparition VARCHAR(255),
                      pv INT,
                      casque VARCHAR(255),
                      plastron VARCHAR(255),
                      pantalon VARCHAR(255),
                      bottes VARCHAR(255),
                      objetTenu VARCHAR(255),
                      nom VARCHAR(255),
                      isBoss BOOLEAN DEFAULT FALSE,
                      slug VARCHAR(255),
                      type VARCHAR(255)
);
