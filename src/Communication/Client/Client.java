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
  private String username;
  private ClientSocket clientSocket;
  private boolean connected = false;
  private String socketMessage;

  public Client(String serverIP, int serverPort) throws UnknownHostException {
    this.serverIP = serverIP;
    this.clientIP = InetAddress.getLocalHost().getHostAddress();
    this.serverPort = serverPort;
    this.clientSocket = new ClientSocket(this.serverPort, this.serverIP, this);
    this.socketMessage = "";
  }

  public String get_IP(){return this.serverIP;}
  public String get_Username(){
    return this.username;
  }
  public String get_Message(){ return this.socketMessage; }
  public String get_ClientIP(){return this.clientIP;}
  public void set_Connected(boolean connected){this.connected = connected;}
  public void set_SocketMessage(String message){this.socketMessage = message;}

  public void clearShell() {
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
    this.clearShell();
    System.out.println("========================\n     Welcoome to P4 Client       \n========================\n");
    while (!request.equals("quit")) {
      System.out.print("Enter command: \n");
      String s = scanner.nextLine();
      String[] commandAndArgs = s.split(" ");
      request = commandAndArgs[0];
      switch (request) {
        case "connect":
          this.clientSocket.sendCommand("connect "+commandAndArgs[1]+ " " + this.clientIP);
          break;
        case "ask":
          this.clientSocket.sendCommand("ask "+commandAndArgs[1]);
          break;
        case "playColumn":
          break;
        case "disconnect":
          this.clientSocket.sendCommand("disconnect "+this.clientIP);
          break;
        case "quit":
          this.clientSocket.sendCommand("disconnect "+this.clientIP);
          this.clientSocket.closeSocket();
          System.out.print("Quitting");
          break;
        default:
          System.out.println("Unknown command");
          break;
      }
    }
    scanner.close();
  }

}
