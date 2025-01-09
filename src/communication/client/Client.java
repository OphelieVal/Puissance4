package communication.client;

import communication.thread.client.ClientSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

//  private List<Plateau> listeMatchs;
//  private int nbVictoires = 0;
//  private double pourcentVict = 0;
//  public void updateVictoires() {
//    this.nbVictoires += 1;
//    this.pourcentVict = this.nbVictoires/this.listeMatchs.size();
//  }

public class Client {
  private String serverIP;
  private String clientIP;
  private int serverPort;
  private String nomJoueur;
  private ClientSocket clientSocket;
  private boolean connecté = false;

  public Client(String serverIP, int serverPort) throws UnknownHostException {
    this.serverIP = serverIP;
    this.clientIP = InetAddress.getLocalHost().getHostAddress();
    this.serverPort = serverPort;
    this.clientSocket = new ClientSocket(this.serverPort, this.serverIP, this);
  }

  /** getter IP server
   * @return IP du serveur
   */
  public String get_IP(){
    return this.serverIP;
  }

  /** getter nom joueur
   * @return nom du joueur
   */
  public String get_nomJoueur(){
    return this.nomJoueur;
  }

  /** getter IP client
   * @return IP du client
   */
  public String get_ClientIP(){
    return this.clientIP;
  }

  /**setter connecte au serveur
   * @param connecté connecte au serveur
   */
  public void set_Connected(boolean connecté){
    this.connecté = connecté;
  }
  
  /**
   * nettoie le terminal 
   */
  public void nettoieTerminal() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        System.out.print("\033[H\033[2J");
        System.out.flush();
      }
    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
    }
  }

  public void execute() throws IOException {
    String request = "";
    Scanner scanner = new Scanner(System.in);
    this.nettoieTerminal();
    System.out.println("========================\n   Bienvenue dans le Puissance 4 - Client     \n========================\n");
    while (!request.equals("quit")) {
      System.out.print("Entrez une commande : \n"+
      "CONNECT NOMJOUEUR pour se connecter à son espace Joueur\n"+
      "QUIT demande la fin de connexion\n");
      String s = scanner.nextLine();
      String[] commandAndArgs = s.split(" ");
      request = commandAndArgs[0].toLowerCase();
      try {
        this.nettoieTerminal();
        switch (request) {
          case "connect":
            nomJoueur = commandAndArgs[1];
            this.clientSocket.clientSocketInit();
            this.clientSocket.start();
            this.clientSocket.sendCommand("\nconnect "+nomJoueur+ " " + this.clientIP);
            this.connected();
            break;
          case "quit":
            this.clientSocket.sendCommand("\ndisconnect "+this.clientIP);
            this.clientSocket.closeSocket();
            System.out.print("Quitting");
            break;
          default:
            System.out.println("Unknown command");
            break;
        }
      }
      catch(Exception e){
        System.err.println("Vous devez rentrer le NOM JOUEUR avec votre commande (CONNECT)\n");
      }
  }
    this.clientSocket.closeSocket();
    scanner.close();
  }

  public void connected(){
    String request = "";
    Scanner scanner = new Scanner(System.in);
    this.nettoieTerminal();
    System.out.println("Vous êtes connecté en tant que " + this.nomJoueur);
    while (!(request.equals("quit"))){
      System.out.println( "ASK demande d'une nouvelle partie avec un joueur\n"+ 
      "DISCONNECT deconnecte de l'espace Joueur\n");
      String s = scanner.nextLine();
      String[] commandAndArgs = s.split(" ");
      request = commandAndArgs[0].toLowerCase();
      try {

        switch (request) {
    
          case "ask":
            this.clientSocket.sendCommand("\nask "+ this.nomJoueur);
            break;
          case "disconnect":
            this.clientSocket.sendCommand("\ndisconnect "+this.clientIP);
            break;
          default:
            System.out.println("Unknown command");
            break;
        }
      }
      catch(Exception e){
        System.err.println(e.getMessage());
      }
    }

    
  }

}
