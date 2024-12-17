import java.io.IOError;
import java.io.IOException;
import java.net.ServerSocket;

class ServerThread extends Thread {
  private Serveur server;
  public ServerThread(Serveur serveur){
    this.server = serveur;
  }
  @Override
  public void run(){
    try {
      ServerSocket socket = new ServerSocket(5000);
      socket.accept();
    }
    catch (IOException e){
      System.err.println(e.getMessage());
    }
    
  }

}
