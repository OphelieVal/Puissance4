import java.net.ServerSocket;

public class SocketHandler extends Thread {
    private Serveur serveur;
    private ServerSocket socket;
    public SocketHandler(Serveur serveur){
        this.serveur = serveur;
    }
    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(serveur.get_port());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
    
}
