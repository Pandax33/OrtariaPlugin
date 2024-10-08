CREATE TABLE joueurs (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         uuid VARCHAR(36) NOT NULL UNIQUE,
                         pseudo VARCHAR(100) NOT NULL,
                         date_first_connexion DATETIME DEFAULT CURRENT_TIMESTAMP,
                         destin ENUM('Demon', 'Ange', 'Dieu', 'Monstre') NOT NULL,
                         classe VARCHAR(50),
                         guilde VARCHAR(50),
                         niveau_aventure INT DEFAULT 0,
                         argent DOUBLE DEFAULT 0.0,
                         points_tdr INT DEFAULT 0,
                         grade VARCHAR(50),
                         niveau_alchimiste INT DEFAULT 0,
                         niveau_arcaniste INT DEFAULT 0,
                         niveau_archeologue INT DEFAULT 0,
                         niveau_cuisinier INT DEFAULT 0,
                         niveau_forgeron INT DEFAULT 0,
                         niveau_pelleteur INT DEFAULT 0,
                         niveau_bucheron INT DEFAULT 0,
                         niveau_mineur INT DEFAULT 0,
                         faveur_astrale int default 10,
                         recharge_astrale int default 1,
                         vitesse int default 0,
                         PV int default 0,
                         force_joueur int default 0

);
ALTER TABLE joueurs
    ADD COLUMN id_guilde INT DEFAULT NULL,
    ADD COLUMN rank_guilde INT DEFAULT NULL,
ADD CONSTRAINT fk_guilde_id FOREIGN KEY (id_guilde) REFERENCES guildes(id);
