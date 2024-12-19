package communication.thread.server;

import communication.serveur.Serveur;
import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketConnector extends Thread {
    private int port = 4444;
    private ServerSocket serverSock;
    private Serveur serveur;

    public ServerSocketConnector(int port, Serveur serveur) throws IOException {
        this.port = port;
        this.serverSock = new ServerSocket(this.port);
        //this.serverSock.bind("0.0.0.0", this.port);
        this.serveur = serveur;
    }

    public void run() {
        try {
            while(true){
                Socket clientSocket = serverSock.accept();
                Thread t = new Thread(new ClientHandler(clientSocket, this.serveur));
                this.serveur.newClientConnected();
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
}
