DROP TABLE IF EXISTS PLAYER;
DROP TABLE IF EXISTS STATS;


CREATE TABLE PLAYER (
    IDP INT PRIMARY KEY AUTO_INCREMENT,
    NOMP VARCHAR(45)
);


CREATE TABLE STATS (
    IDP INT PRIMARY KEY, 
    NBVICTOIRES INT DEFAULT 0,
    NBPARTIES INT DEFAULT 0,
    POURCENTVICT FLOAT DEFAULT 0,
    CHECK (POURCENTVICT = NBVICTOIRES/NBPARTIES)
);

ALTER TABLE STATS ADD FOREIGN KEY (IDP) REFERENCES PLAYER(IDP);
