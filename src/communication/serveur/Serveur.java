package communication.serveur;


import communication.thread.server.ServerSocketConnector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modele.Plateau;


public class Serveur {
  private final int port;
  private List<Plateau> lesPlateaux;
  private List<PlayerClient> clientsJoueurs;
  private List<PlayerClient> attenteClients;
  private ServerSocketConnector socketConnexion;
  private Random rand = new Random();

  public Serveur(int port) throws IOException {
    this.port = port;
    this.lesPlateaux = new ArrayList<>();
    this.clientsJoueurs = new ArrayList<>();
    this.socketConnexion = new ServerSocketConnector(port, this);
    this.socketConnexion.start();
  }

  /** getter port
   * @return port du serveur
   */
  public int get_port(){
    return this.port;
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
   * @param clientIP
   * @return true si la demande a été prise en compte et realisé
   * sinon false
   */
  public boolean disconnect(String clientIP) {
    for (PlayerClient client : this.clientsJoueurs) {
      if (client.getClientIP().equals(clientIP)) {
        this.clientsJoueurs.remove(client);
        return true;
      }
    }
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

  public void newClientConnected() {
      System.out.println("Un nouveau client est connecté");
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
  public PlayerClient getClientAttente(){
    for (PlayerClient client : this.attenteClients){
      if (client.getClientPlayer().getPlateau()==null){
        return client;
      }
    }
    return null;

  }

//---------------------------------------------------------------------------------

  public String waitClient(PlayerClient player) {
    this.attenteClients.add(player);
    return "EN ATTENTE d'autres joueurs";
  }

  public String ask(String nomJoueur) {
    PlayerClient client1 = this.getClient(nomJoueur);
    if (client1==null){
      return "ERR Non connecté";
    }
    PlayerClient client2 = this.getClientAttente();
    if (client2==null){
      this.waitClient(client1);
      return "Veuillez patienter";
    }
    Plateau plateau = new Plateau(6, 7);
    client1.getClientPlayer().setLePlateau(plateau);
    client2.getClientPlayer().setLePlateau(plateau);
    return "OK Plateau initialisé";
  }

  public String play(int nomColonne) {
    // Ajouter match/plateau dans PlayerClient.listeMatchs
    return "";
  }

  public String update(String nomJoueur) {
    return "";
  }

  public boolean win(String nomJoueur) {
    // PlayerClient client = this.getClient(nomJoueur);
    // client.updateVictoires();
    return false;
  }

  public String end(String nomJoueur) {
    return "";
  }

  public String getStats(String nomJoueur) {
    PlayerClient client = this.getClient(nomJoueur);
    int nbVict = client.getNbVictoires();
    double pourcentVict = client.getPourcentVict();
    int nbMatchs = client.getNbMatchs();
    String res = "Voici les statistiques du joueur" + nomJoueur + 
    "\n Nombre de victoires :" + nbVict + "Nombre de matchs joués :" +
    nbMatchs + "\n Pourcentage de victoires :" + pourcentVict;
    return res;
  }

  /** demande la fin de connexion d'un joueur
   * @param nomJoueur nom d'un joueur
   * @return ERR si erreur, sinon BYE
   */
  public String quit(String nomJoueur) {
    return "";
  }

}
