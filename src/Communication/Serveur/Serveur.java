package communication.serveur;


import communication.thread.server.ServerSocketConnector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modele.Joueur;
import modele.Plateau;


public class Serveur {
  private final int port;
  private List<Plateau> lesPlateaux;
  private List<PlayerClient> players_client;
  private ServerSocketConnector socketConnector;
  private Random rand = new Random();

  public Serveur(int port) throws IOException {
    this.port = port;
    this.lesPlateaux = new ArrayList<>();
    this.players_client = new ArrayList<>();
    this.socketConnector = new ServerSocketConnector(port, this);
    this.socketConnector.start();
  }

  public int get_port(){return this.port;}

  public boolean clientIsConnected(String username, String clientIP) {
    return this.players_client.contains(new PlayerClient(username, clientIP));
  }

  public boolean connect(String username, String clientIP) throws IOException {
    if (this.clientIsConnected(username, clientIP)) {
      return false;
    }
    for (PlayerClient client : this.players_client) {
      if (client.getUsername().equals(username)) {
        return false;
      }
    }
    this.players_client.add(new PlayerClient(username, clientIP));
    return true;
  }

  public boolean disconnect(String clientIP) {
    for (PlayerClient client : this.players_client) {
      if (client.getClientIP().equals(clientIP)) {
        this.players_client.remove(client);
        return true;
      }
    }
    return false;
  }

  public String showConnectedClients() {
    String res = "\n";
    for (PlayerClient client : this.players_client) {
      res += client.toString() + System.lineSeparator();
    }
    return res;
  }

  public void ajouter_plateau(Plateau plateau){
    this.lesPlateaux.add(plateau);
  }

  public void newClientConnected() {
      System.out.println("a new client connected");
  }

//---------------------------------------------------------------------------------

  public String wait(String nomJoueur) {
    return "";
  }

  public String ask(String nomJoueur) {
    return "";
  }

  public String play(int nomColonne) {
    return "";
  }

  public String update(String nomJoueur) {
    return "";
  }

  public boolean win(String nomJoueur) {
    return false;
  }

  public String end(String nomJoueur) {
    return "";
  }

  public String getStats(String nomJoueur) {
    return "";
  }

  public String quit(String nomJoueur) {
    return "";
  }

}
