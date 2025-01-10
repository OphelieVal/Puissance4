package communication.thread.server;

import communication.serveur.PlayerClient;
import communication.serveur.Serveur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private InputStreamReader stream;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private String clientInetAdress;
    private Serveur serveur;

    public ClientHandler(Socket socket, Serveur server) throws IOException {
        this.serveur = server;
        this.socket = socket;
        this.stream = new InputStreamReader(this.socket.getInputStream());
        this.reader = new BufferedReader(stream);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.clientInetAdress = socket.getInetAddress().getHostAddress();
    }

    public String getClientIP() {
        return this.clientInetAdress;
    }

    public void serverLog(String message) {
        String timeStamp = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp + " : "+ message);
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    @Override
    public void run() {
        String message = "";
        try {
            while (true) {
                if (socket.isClosed()) {
                    break;
                } else {
                    try {
                        message = reader.readLine();
                        String[] args = message.split(" ");
                        boolean status;
                        if (args.length >= 2) {
                            switch (args[0]) {
                                case "connect":
                                        serverLog("received connection request from: "+this.clientInetAdress +" to username: "+args[1]);
                                        status = this.serveur.clientIsConnected(args[1], this.clientInetAdress);
                                        if (status) {
                                            this.writer.println("\nDeja connecte "+args[1]+" OK");
                                            serverLog("clients connectes: "+this.serveur.showConnectedClients());
                                        }else if (this.serveur.connect(args[1], this.clientInetAdress)) {
                                            this.writer.println("\nconnecté "+args[1]+" OK");
                                            serverLog("clients connectes: "+this.serveur.showConnectedClients());
                                        }else {
                                            this.writer.println("\nERR connexion à  "+args[1]+" refusé ou échoué");
                                        }
                                        break;
                                case "ask":
                                    String joueur = args[1];
                                    PlayerClient client = this.serveur.ask(joueur);
                                    if (client==null){
                                        this.writer.println("EN ATTENTE d'autres joueurs");
                                    }
                                    else {
                                        this.writer.println("OK plateau initilialisé");
                                    }
                                    break;
                                case "disconnect":
                                    serverLog("received disconnect request from: "+this.clientInetAdress);
                                    status = this.serveur.disconnect(this.clientInetAdress);
                                    if (status) {
                                        System.out.println("\nclients connectés "+this.serveur.showConnectedClients());
                                        this.writer.println("\ndéconnecté");
                                        this.writer.println("\nexit");
                                        this.socket.close();
                                    }else {
                                        this.writer.println("\nERR Vous n'êtes pas connecté en tant que joueur pour "+this.clientInetAdress);
                                    }
                                    break;
                                default:
                                    this.sendMessage("\nERR commande non connue");
                                    break;
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }catch (Exception e) {
            System.err.println(e.getMessage()+e.getCause());
            System.err.println("Déconnexion client inattendu\n");
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
