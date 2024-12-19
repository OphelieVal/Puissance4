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
  }

}
