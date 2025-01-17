# Puissance4

VALIN Ophélie
BOCQUET Clémence
AKHTAR Naima
RANDRIANTSOA Nathan

## Prérequis
Pour lancer l'application :
- installation Java
- Driver JDBC et la base de données mysql (optionnel)

## Lancement
Compilation en ligne de commande depuis la racine du projet : 
-  javac -d ../bin/ src/BD/bd/*.java src/modele/*.java src/communication/client/*.java src/communication/serveur/*.java src/communication/thread/client/*.java src/communication/thread/server/*.java src/executable/*.java src/modele/*.java
- depuis VisualStudio Code : compile automatique (juste à lancer les executables)

Lancer le projet :
- java -cp bin:pathtodriverJDBC  executable/server
- java -cp bin executable/client1 
-  java -cp bin executable/client2

## Fonctionnalités
- Vous pouvez vous connecter en tant que joueur avec CONNECT + nomJoueur (et si vous déconnecter avec DISCONNECT)
- Vous pouvez demander une nouvelle partie avec ASK
- Pour jouer, attendez votre tour et saisissez la commande PLAY + nb Colonne
- Si la partie est terminé, vous pouvez retourner au menu avec HOME
- Vous pouvez voir vos statistiques avec PLAYERSTATS
