package communication.serveur;

import communication.thread.server.ServerSocketConnector;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


import BD.bd.*;
import modele.*;


public class Serveur {
  private final int port;
  private List<Plateau> lesPlateaux;
  private List<PlayerClient> clientsJoueurs;
  private List<PlayerClient> attenteClients;
  private ServerSocketConnector socketConnexion;
  private Random rand = new Random();
  private final PlayerBD playerBD;
  private final StatsBD statsBD;

  public Serveur(int port) throws IOException {
    this.port = port;
    this.lesPlateaux = new ArrayList<>();
    this.clientsJoueurs = new ArrayList<>();
    this.attenteClients = new ArrayList<>();
    this.socketConnexion = new ServerSocketConnector(port, this);
    this.socketConnexion.start();
    this.playerBD = new PlayerBD();
    this.statsBD = new StatsBD();
  }

  /** getter port
   * @return port du serveur
   */
  public int get_port(){
    return this.port;
  }
  /**
   * getter player base de donnees
   * @return le player modele
   */
  public PlayerBD getPlayerBD(){
    return this.playerBD;
  }

  /**
   * getter stats base de donnees
   * @return le player modele
   */
  public StatsBD getstatsBd(){
    return this.statsBD;
  }

  /** verifie si le client est deja connecte au serveur 
   * @param nomJoueur nom du joueur
   * @param clientIP IP du client
   * @return true si le client est deja connecte au serveur
   */
  public boolean clientIsConnected(String nomJoueur, String clientIP) {
    return this.clientsJoueurs.contains(new PlayerClient(nomJoueur, clientIP));
  }

  /** connecte le client avec un nom joueur different
   * @param nomJoueur
   * @param clientIP
   * @return true si le client est connecté sinon false
   * @throws IOException
   */
  public boolean connect(String nomJoueur, String clientIP) throws IOException {
    if (this.clientIsConnected(nomJoueur, clientIP)) {
      return false;
    }
    for (PlayerClient client : this.clientsJoueurs) {
      if (client.getNomJoueur().equals(nomJoueur)) {
        return false;
      }
    }
    this.clientsJoueurs.add(new PlayerClient(nomJoueur, clientIP));
    return true;
  }

  /** deconnecte le client du serveur
   * @param clientIP IP du client a deconnceter de son utilisateur
   * @return true si la demande a été prise en compte et realisé
   * sinon false
   */
  public boolean disconnect(String clientIP) {
    this.serverLog("Attempting to disconnect client with IP: " + clientIP);
    for (PlayerClient client : this.clientsJoueurs) {
      this.serverLog("Checking client: " + client.getClientIP());
      if (client.getClientIP().equals(clientIP)) {
        this.clientsJoueurs.remove(client);
        this.serverLog("Client disconnected: " + clientIP);
        return true;
      }
    }
    this.serverLog("Client not found: " + clientIP);
    return false;
  }

  /** affiche l'ensemble des clients connectes au serveur
   * @return ensemble clients connectes au serveur
   */
  public String showConnectedClients() {
    String res = "\n";
    for (PlayerClient client : this.clientsJoueurs) {
      res += client.toString() + System.lineSeparator();
    }
    return res;
  }

  public void ajouter_plateau(Plateau plateau){
    this.lesPlateaux.add(plateau);
  }

  public void serverLog(String message) {
    String timeStamp = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(Calendar.getInstance().getTime());
    System.out.println(timeStamp + " : "+ message);
  }

  public void newClientConnected(String ip) {
      serverLog("Un nouveau client est connecté avec l'ip: "+ip);
  }

  public PlayerClient getClient(String nomJoueur){
    for (PlayerClient client : this.clientsJoueurs){
      if (client.getNomJoueur().equals(nomJoueur)){
        return client;
      }
    }
    return null;
  }

  /**
   * get un client en attente
   * @return un client en attente
   */
  public PlayerClient getClientAttente(PlayerClient excludedClient){
    for (PlayerClient client : this.attenteClients){
      if (client.getClientPlayer().getPlateau()==null && !(client.equals(excludedClient))){
        return client;
      }
    }
    return null;

  }

//---------------------------------------------------------------------------------
  /**
   * met le joueur en attente
   * @param player joueur a mettre en attente
   * @return string en attente
   */
  public String waitClient(PlayerClient player) {
    this.attenteClients.add(player);
    return "EN ATTENTE d'autres joueurs";
  }

  public String removeWaitClient(PlayerClient client) {
    boolean status = false;
    if (this.attenteClients.contains(client)){
      this.attenteClients.remove(client);
      status = true;
    }
    if (!status) {
      return "ERR Joueur non present dans la fill d'attente";
    }else {
      return "Joueur: "+client.getNomJoueur()+ "supprimé de la file d'attente";
    }
  }

  /** verifie si le joueur est dans la file d'attente */
  public boolean isPlayerInAwaitingQueue(String nomJoueur, String clientIP) {
    for (PlayerClient client : this.attenteClients) {
      if (client.getNomJoueur().equals(nomJoueur) && client.getClientIP().equals(clientIP)) {
        return true;
      }
    }
    return false;
  }

  /**
   * demande une nouvelle partie contre un joueur au hasard
   * @param nomJoueur joueur demandant un adversaire
   * @return null si aucun joueur sinon nouveau joueur (adversaire)
   */
  public PlayerClient ask(String nomJoueur) {
    PlayerClient client1 = this.getClient(nomJoueur);
    PlayerClient client2 = this.getClientAttente(client1);
    if (client2==null){
      this.waitClient(client1);
      return null;
    }
    Plateau plateau = new Plateau(6, 7, client1.getClientPlayer(), client2.getClientPlayer()); // initialise à un plateau de taille normale
    client1.getClientPlayer().setLePlateau(plateau);
    client1.getClientPlayer().setCouleur("rouge");
    client2.getClientPlayer().setLePlateau(plateau);
    client2.getClientPlayer().setCouleur("jaune");
    plateau.setTurn(nomJoueur);
//    this.removeWaitClient(client1);
    this.removeWaitClient(client2);
    return client2;
  }

  public String getInGamePlateau(String username) {
    return this.getClient(username).getClientPlayer().getPlateau().toString();
  }

  /**
   * depose le jeton du joueur sur le plateau 
   * @param nomColonne colonne ou le jeton est pose
   * @param nomJoueur nom du joueur jouant
   * @return ERR si plus de place ou joueur non toruvé, sinon OK
   */
  public String play(int nomColonne, String nomJoueur) {
    boolean pose = false;
    Joueur joueur =  this.getClient(nomJoueur).getClientPlayer();
    try {
      if (joueur.getNomJoueur()!=null){
        pose = joueur.getPlateau().poseJeton(nomColonne, joueur);
      }
    }
    catch (GagnantException e){
      this.win(joueur);
    }
    catch (EnDehorsDuPlateauException e){
      return "ERR plus de place sur cette colonne";
    }
    catch (OccupeeException e){
      return "Il y a déjà un jeton";
    }
    if (pose){
      return "OK jeton posé";
    }
    return "ERR nomJoueur invalide";
  }

  /**
   * verifie si le joueur a gagné
   * @param joueur
   * @return true si a gagné sinon false
   */
  public boolean win(Joueur joueur) {
    System.out.println(joueur.getNomJoueur() + " a gagné la partie ");
    return true;
  }

//  public void forceEndGame(String clientIP) throws InGamePLayerDisconnected {
//    for (PlayerClient client : this.clientsJoueurs) {
//      if (client.getClientIP().equals(clientIP)) {
//        Plateau plateau = client.getClientPlayer().getPlateau();
//        if (plateau != null) {
//          plateau.signalPlayerDisconnected();
//        }
//      }
//    }
//  }

  /**
   * fin de partie du joueur = quitte le plateau de jeu
   * @param nomJoueur
   * @return chaine de caractere : OK si plateau quitté sinon ERR
   */
  public String end(String nomJoueur) {
    PlayerClient client = this.getClient(nomJoueur);
    if (client==null){
      return "ERR joueur non recoonu";
    }
    client.getClientPlayer().setLePlateau(null);
    return "OK fin de partie";
  }

  /**
   * donne les statistiques d'un joueur (nb parties, nb gagnantes..)
   * @param nomJoueur
   * @return statistique d'un joueur
   */
  public String getStats(String nomJoueur) {
    return "";
  }

  /** demande la fin de connexion d'un joueur
   * @param nomJoueur nom d'un joueur
   * @return ERR si erreur, sinon BYE
   */
  public String quit(String nomJoueur) {
    PlayerClient client = this.getClient(nomJoueur);
    if (client==null){
      return "ERR le client n'existe pas";
    }
    if (this.attenteClients.contains(client)){
      this.attenteClients.remove(client);
    }
    else  {
      this.clientsJoueurs.remove(client);
    }
    return "BYE";
  }

}