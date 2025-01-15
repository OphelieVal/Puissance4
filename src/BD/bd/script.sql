DROP TABLE IF EXISTS PLAYER;

CREATE TABLE PLAYER (
    pseudo varchar(10),
    couleurp varchar(10),
    connecte boolean,
    NbVictoires int,
    NbParties int
);