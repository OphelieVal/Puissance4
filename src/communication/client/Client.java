package communication.client;

import communication.thread.client.ClientSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {
  private final String serverIP;
  private final String clientIP;
  private final ClientSocket clientSocket;
  private String nomJoueur;
  private ClientState clientState = ClientState.USERDISCONNECTED;
  private boolean logs = false;

  public Client(String serverIP, int serverPort) throws UnknownHostException {
    this.serverIP = serverIP;
    this.clientIP = InetAddress.getLocalHost().getHostAddress();
    this.clientSocket = new ClientSocket(serverPort, this.serverIP, this);
  }

  public Client(String serverIP, int serverPort, boolean logs) throws UnknownHostException {
    this.serverIP = serverIP;
    this.clientIP = InetAddress.getLocalHost().getHostAddress();
    this.logs = logs;
    this.clientSocket = new ClientSocket(serverPort, this.serverIP, this, logs);
  }

  /** getter nom joueur
   * @return nom du joueur
   */
  public String get_nomJoueur(){return this.nomJoueur;}

  public void setNomJoueur(String nomJoueur){this.nomJoueur = nomJoueur;}

  /** getter IP client
   * @return IP du client
   */
  public synchronized String get_ClientIP(){return this.clientIP;}

  /**
   * getter Etat du client
   * @return Etat actuel du client
   */
  public synchronized ClientState get_ClientState(){return this.clientState;}

  /**
   * Setter Etat client future
   * @param clientState Etat future
   */
  public synchronized void set_ClientState(ClientState clientState){this.clientState = clientState;}

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
  public void updateTerminal(ClientState actualState) throws IOException, InterruptedException {
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
          "-> DISCONNECT deconnecte de l'espace Joueur\n" +
          "-> PLAYERSLIST donne la liste de tout les joueur actuellement connecte\n" +
          "-> ALLPLAYERSLIST donne la liste de tout les joueur enregistrer sur le jeu\n" +
          "-> PLAYERSTATS <username> deconnecte de l'espace Joueur\n";
        break;
      case ClientState.INGAME:
        String[] args = new String[] {"getactualplate", this.nomJoueur} ;
        this.request("getactualplate", args, this.clientState == ClientState.INGAME);
        terminal = "Vous avez rejoint une nouvelle partie\nVous posséder un plateau de 6 lignes et 7 colonnes\nPour jouer, entrez PLAY + le numéro de la colonne (0 à 6) où vous déposez un jeton\nQUIT pour quitter";
        break;
      case ClientState.LOOKINGADVERSARY:
        terminal = "AWAITING QUERY\n";
        break;

      case ClientState.WAITGAME:
        terminal = "Ce n'est as votre tour, patientez..\n";
        if (this.clientState == ClientState.LOOKINGADVERSARY) {
          this.request("waitgame", new String[]{"play",this.nomJoueur}, true);
          this.clientSocket.clientLog("state: "+this.clientState);
          Thread.sleep(500);
          this.updateTerminal(this.clientState);
        }
        break;
      case ClientState.ENDGAME:
        terminal = "Entrez une commande : \n"+
                "-> HOME retour a l'ecran d'acceuil\n";
        break;
      default:
        terminal = "NOTHING";
        break;
    }
    this.nettoieTerminal();
    System.out.println("Actual client state: "+this.get_ClientState());
    if (!(this.nomJoueur == null) && this.clientState != ClientState.USERDISCONNECTED) {
      System.out.println("Connecte en tant que joueur: "+this.nomJoueur);
    }
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
//          this.clientSocket.clientLog("[logs] one or more param in request");
          prepare += " ";
          for (int i = 1; i < args.length; i++) {
            prepare += args[i] + " ";
          }
        }
//        this.clientSocket.clientLog(prepare);
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
          throw new RuntimeException("\n Assurez vous que l'adresse IP du serveur est bonne ou bien qu'il est lance" +
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

        try {
            this.updateTerminal(this.clientState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String s = scanner.nextLine();
      String[] commandAndArgs = s.split(" ");
      request = commandAndArgs[0].toLowerCase();

      try {
        this.nettoieTerminal();
        switch (request) {
          case "connect":
            if (commandAndArgs.length != 2 ) throw new IOException("La commande ask doit avoir 2 argument");
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
            this.ask_client(commandAndArgs);
            break;

          case "play":
            this.play_client(commandAndArgs);

            break;
          case "disconnect":
            if (commandAndArgs.length > 1) throw new IOException("Il ne doit pas avoir d'argument pour cette commande");
            this.request("disconnect", commandAndArgs, this.clientState == ClientState.USERCONNECTED);
            break;

          case "home":
            if (commandAndArgs.length != 1) throw new IOException("Il ne doit pas avoir d'argument pour cette commande");
            this.request("home", commandAndArgs, this.clientState == ClientState.ENDGAME);
            break;

          case "playerstats":
            if (commandAndArgs.length != 2) throw new IOException("La commande playerstats doit avoir 1 argument <nomDuJoueur>");
            this.request("playerstats", commandAndArgs, this.clientState == ClientState.USERCONNECTED);
            break;

          case "playerslist":
            if (commandAndArgs.length != 1) throw new IOException("Il ne doit pas avoir d'argument pour cette commande");
            this.request("playerstats", commandAndArgs, this.clientState == ClientState.USERCONNECTED);
            break;

          case "allplayerslist":
            if (commandAndArgs.length != 1) throw new IOException("Il ne doit pas avoir d'argument pour cette commande");
            this.request("allplayerslist", commandAndArgs, this.clientState == ClientState.USERCONNECTED);
            break;

          default:
            throw new IOException("Unknown command");
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

  public void request_turn() throws IOException, InterruptedException {
    long currentTime = System.currentTimeMillis();
    while (this.clientState == ClientState.WAITGAME) {
      this.request("waitgame", new String[]{"play",this.nomJoueur}, true);
      this.clientSocket.clientLog("state: "+this.clientState);
      this.clientSocket.clientLog("Durée écoulé: "+ (System.currentTimeMillis()-currentTime) + " ms");
      Thread.sleep(500);
    }
  }

  public void request_adversary() throws IOException, InterruptedException {
    String[] none = new String[] {"isawait", this.nomJoueur};
    long currentTime = System.currentTimeMillis();
    while (this.clientState == ClientState.LOOKINGADVERSARY) {
      this.request("isawait", none, true);
      this.clientSocket.clientLog("state: "+this.clientState);
      this.clientSocket.clientLog("Durée écoulé: "+ (System.currentTimeMillis()-currentTime) + " ms");
      Thread.sleep(500);
    }
  }

  /**
   * gere la commande ask du client
   * @param commandAndArgs
   * @throws IOException
   * @throws InterruptedException
   */
  public void ask_client(String[] commandAndArgs) throws IOException, InterruptedException{
    if (commandAndArgs.length != 1) throw new IOException("La commande ask ne doit pas avoir d'arguments ");
    String[] arguments = new String[] {"ask", this.nomJoueur};
    this.request("ask", arguments, this.clientState == ClientState.USERCONNECTED);
    Thread.sleep(1000);

    this.request_adversary();
    Thread.sleep(1000);

    this.request_turn();
    String[] args = new String[] {"getactualplate", this.nomJoueur} ;
    this.request("getactualplate", args, this.clientState == ClientState.INGAME);
  }

  /**
   * gere la commande connect du client
   * @param commandAndArgs
   * @throws IOException
   * @throws InterruptedException
   */
  public void play_client(String[] commandAndArgs)throws IOException, InterruptedException{
    if (commandAndArgs.length < 2) throw new IOException("Veuillez rentrer le numéro de la colonne à jouer");
    System.out.println(commandAndArgs.length+"");
    String colonne = commandAndArgs[1];
    try {
      Integer.parseInt(colonne);
      String[] commandAndArgsPlay = new String[] {commandAndArgs[0], commandAndArgs[1], this.nomJoueur};
      this.request("play", commandAndArgsPlay, this.clientState == ClientState.INGAME);
      Thread.sleep(1000);
      this.request_turn();
    }
    catch (NumberFormatException e){
      throw new IOException("Veuillez entrer un nombre");
    }
  }
}



