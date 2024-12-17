import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler extends Thread {
  private Client client;
  public ClientHandler(Client client){
    this.client = client;
  }
  @Override
  public void run(){
    try {
          Socket clientSocket = new Socket(this.client.get_IP(), 5000);

          BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch (Exception e){
          System.err.println(e.getMessage());
        }
  }

}
