package communication.client;

import communication.thread.client.ClientSocket;
import java.net.InetAddress;

import java.io.IOException;
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
    this.clientSocket.clientSocketInit();
    this.clientSocket.start();
    this.nettoieTerminal();
    System.out.println("========================\n   Bienvenue dans le Puissance 4 - Client     \n========================\n");
    while (!request.equals("quit")) {
      System.out.print("Entrez une commande : \n"+
      "connect pour se connecter à son espace Joueur\n"+
      "ask demande d'une nouvelle partie avec un joueur\n"+
      "disconnect deconnecte de l'espace Joueur\n"+
      "quit demande la fin de connexion\n");
      String s = scanner.nextLine();
      String[] commandAndArgs = s.split(" ");
      request = commandAndArgs[0];
      switch (request) {
        case "connect":
          try {
            this.clientSocket.sendCommand("\nconnect "+commandAndArgs[1]+ " " + this.clientIP);
          }
          catch (IndexOutOfBoundsException e){
            System.err.println("\nVous devez entre le nom du joueur\n");
          }
          break;
        case "ask":
          this.clientSocket.sendCommand("\nask "+commandAndArgs[1]);
          break;

        case "disconnect":
          this.clientSocket.sendCommand("\ndisconnect "+this.clientIP);
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
    this.clientSocket.closeSocket();
    scanner.close();
  }

}
