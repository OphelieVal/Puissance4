package communication.client;

import com.sun.source.doctree.EscapeTree;
import communication.thread.client.ClientSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {
  private final String serverIP;
  private final String clientIP;
  private final ClientSocket clientSocket;
  private int serverPort;
  private String nomJoueur;
  private ClientState clientState = ClientState.USERDISCONNECTED;

  public Client(String serverIP, int serverPort) throws UnknownHostException {
    this.serverIP = serverIP;
    this.clientIP = InetAddress.getLocalHost().getHostAddress();
    this.serverPort = serverPort;
    this.clientSocket = new ClientSocket(this.serverPort, this.serverIP, this);
  }

  /** getter IP server
   * @return IP du serveur
   */
  public String get_ServerIP(){
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
  public synchronized String get_ClientIP(){
    return this.clientIP;
  }

  /**
   * getter Etat du client
   * @return Etat actuel du client
   */
  public synchronized ClientState get_ClientState(){
    return this.clientState;
  }

  /**
   * Setter Etat client future
   * @param clientState Etat future
   */
  public synchronized void set_ClientState(ClientState clientState){
    this.clientState = clientState;
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

  /**
   * Permet de mettre a jour le terminal en fonction de l'etat du client
   * @param actualState
   */
  public void updateTerminal(ClientState actualState) {
    String terminal = "";
    switch (actualState) {
      case ClientState.USERDISCONNECTED:
        terminal = "Entrez une commande : \n"+
          "-> CONNECT NOMJOUEUR pour se connecter à son espace Joueur\n"+
          "-> QUIT demande la fin de connexion\n";
        break;
      case ClientState.USERCONNECTED:
        terminal = "Entrez une commande : \n"+
          "-> ASK demande d'une nouvelle partie avec un joueur\n"+
          "-> DISCONNECT deconnecte de l'espace Joueur\n";
        break;
      case ClientState.INGAME:
        terminal = "NO COMMANE\n";
      case ClientState.LOOKINGADVERSARY:
        terminal = "NOT IMPLEMENTED\n";
      default:
        terminal = "NOTHING";
    }
    this.nettoieTerminal();
    System.out.println("Actual client state: "+this.get_ClientState());
    System.out.println(terminal);
  }

  /**
   * Prepare la requete qui sera envoye au server distant
   * @param command La commande a effectue
   * @param args list de string contenant la commande et le argument
   * @param executeCondition condition d'execution de la requete
   * @throws IOException Exception
   */
  public void request(String command, String[] args, boolean executeCondition) throws IOException, InterruptedException {
      if (executeCondition) {
        String prepare = "\n"+command;
        if (args.length > 1) {
          System.out.println("[logs] one or more param in request");
          prepare += " ";
          for (int i = 1; i < args.length; i++) {
            prepare += args[i] + " ";
          }
        }
        //System.out.println(prepare);
        this.clientSocket.sendCommand(prepare);
        Thread.sleep(1500);
      }else {
        System.out.println("Unknown command");
      }
  }

  /**
   * Methode principal, lancement du client
   * @throws IOException
   */
  @Override
  public void run()  {
      try {
          this.clientSocket.clientSocketInit();
      } catch (Exception e) {
          throw new RuntimeException(e.getMessage() + "\n Assurez vous que l'adresse IP du serveur est bonne ou bien qu'il est lance" +
                  "\npour trouver l'adresse du serrver rendez vous sur la machine de celui-ci et ouvrez un terminal entrez: ipconfig (si windows) ou ifconfig" +
                  "\nregarder l'IPv4 de l'interface que vous utiliser (eth0 pour carte reseau ethernet / Wifi pour carte Wi-fi)");
      }
    this.clientSocket.start();
    boolean quit = false;
    this.nettoieTerminal();
    String request = "";
    Scanner scanner = new Scanner(System.in);
    System.out.println("========================\n   Bienvenue dans le Puissance 4 - Client     \n========================\n");
    while (!quit) {

      this.updateTerminal(this.clientState);
      String s = scanner.nextLine();
      String[] commandAndArgs = s.split(" ");
      request = commandAndArgs[0].toLowerCase();

      try {
        this.nettoieTerminal();
        switch (request) {
          case "connect":
            if (commandAndArgs.length > 2) throw new IOException("La commande ask doit avoir 2 argument");
            this.request("connect", commandAndArgs, this.clientState == ClientState.USERDISCONNECTED);
            break;

          case "quit":
            if (commandAndArgs.length > 1) throw new IOException("Il ne doit pas avoir d'argument pour cette commande");
            this.request("disconnect", commandAndArgs, this.clientState == ClientState.USERDISCONNECTED);
            this.clientSocket.closeSocket();
            System.out.print("Quitting");
            quit = true;
            break;

          case "ask":
            if (commandAndArgs.length != 2) throw new IOException("La commande ask doit avoir 1 argument 'ASK <nom du Joueur>'");
            this.request("ask", commandAndArgs, this.clientState == ClientState.USERCONNECTED);
            break;

          case "disconnect":
            if (commandAndArgs.length > 1) throw new IOException("Il ne doit pas avoir d'argument pour cette commande");
            this.request("disconnect", commandAndArgs, this.clientState == ClientState.USERCONNECTED);
            break;

          default:
            System.out.println("Unknown command");
            break;
        }
      }


      catch(IOException e){
        e.fillInStackTrace();
        System.err.println(e.getMessage());
      }

      catch (InterruptedException e) {
          throw new RuntimeException(e);
      }

    }
      try {
          this.clientSocket.closeSocket();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
      scanner.close();
  }
}
