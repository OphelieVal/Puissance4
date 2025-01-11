package communication.thread.server;

import communication.serveur.PlayerClient;
import communication.serveur.Serveur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private InputStreamReader stream;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final String clientInetAdress;
    private final Serveur serveur;
    private String username;

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

    /**
     * Permer d'afficher un log dans la console du server
     * forme [yyyy-MM-dd HH:mm:ss] + message
     * @param message message du log
     */
    public void serverLog(String message) {
        String timeStamp = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp + " : "+ message);
    }

    /**
     * envoie un message qui sera affiche sur le terminal du client connecte
     * @param message message qui sera affiche
     */
    public void sendMessage(String message) {
        String prepare = "\ntype:serverMessage|";
        writer.println(prepare + message);
        writer.flush();
    }

    /**
     * envoie une instruction d'action au client connecte comme un changenment d'etat.
     * Contrairement a l'envoie de message une instruction n'affiche rien sur le terminal du client
     * @param instruction instriction a realiser qui sera comparer au client distant
     */
    public void sendClientInstruction(String instruction) {
        String prepare = "\ntype:clientInstruction|";
        writer.println(prepare + instruction);
        writer.flush();
    }

    /**
     * Envoie une reponse au client via le reseau, la reponse peut etre de type different :
     * - Instrction
     * - Message
     * @param type type de la reponse a envoyer
     * @param statement reponse qui sera envoyer
     */
    public void sendResponse(String type, String statement) {
        switch (type) {
            case "serverMessage":
                this.sendMessage(statement);
                break;
            case "clientInstruction":
                this.sendClientInstruction(statement);
                break;
        }
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
                        if (!message.isEmpty()) {
                            this.serverLog("received interaction: [" + message + "] from client: "+this.clientInetAdress +
                                    " | command and args numbers: "+args.length);
                            boolean status;
                            String indicator = args[0];
                            switch (indicator.toLowerCase()) {

                                case "connect":
                                        serverLog("received connection request from: "+this.clientInetAdress +" to username: "+args[1]);
                                        status = this.serveur.clientIsConnected(args[1], this.clientInetAdress);
                                        if (status) {
                                            this.sendResponse("serverMessage", "Deja connecte "+args[1]+" OK");
                                            serverLog("clients connectes: "+this.serveur.showConnectedClients());
                                        }else if (this.serveur.connect(args[1], this.clientInetAdress)) {
                                            this.sendResponse("clientInstruction", "INITUSERNAME "+args[1]+" OK");
                                            this.sendResponse("clientInstruction", "SET USERCONNECTED STATE WITH "+args[1]+" OK");
                                            this.sendResponse("serverMessage", "connecté "+args[1]+" OK");
                                            serverLog("clients connectes: "+this.serveur.showConnectedClients());
                                        }else {
                                            this.sendMessage("ERR connexion à  "+args[1]+" refusé ou échoué");
                                        }
                                    break;

                                case "ask":
                                    String joueur = args[1];
                                    PlayerClient client = this.serveur.ask(joueur);
                                    if (client==null){
                                        this.sendResponse("clientInstruction", "SET LOOKINGADVERSARY STATE OK");
                                        this.sendResponse("serverMessage", "EN ATTENTE d'autres joueurs");
                                    }
                                    else {
                                        this.sendResponse("serverMessage", "ADVERSAIRE TROUVE USERNAME: "+client.getNomJoueur() + " | " +
                                                "IP: " + client.getClientIP());
                                        this.sendResponse("serverMessage", "OK plateau initilialisé");
                                        this.sendResponse("clientInstruction", "SET INGAME STATE OK");
                                    }
                                    break;

                                case "isawait":
                                    status = this.serveur.isPlayerInAwaitingQueue(args[1], this.clientInetAdress);
                                    System.out.println(Arrays.toString(this.serveur.getClientAttenteList().toArray()));
                                    if (status) {
                                        this.sendResponse("serverMessage", "LOOKING FOR ANOTHER PLAYER TO JOIN...");
                                    }else {
                                        this.sendResponse("clientInstruction", "SET INGAME STATE OK");
                                        this.sendResponse("serverMessage", "GAME FOUND");
                                        this.sendResponse("serverMessage", "STARTING...");
                                    }
                                    break;

                                case "disconnect":
                                    serverLog("received disconnect request from: "+this.clientInetAdress);
                                    status = this.serveur.disconnect(this.clientInetAdress);
                                    if (status) {
                                        this.sendResponse("clientInstruction", "INITUSERNAME NULL OK");
                                        this.sendResponse("clientInstruction", "SET USERDISCONNECTED STATE OK");
                                        this.sendResponse("serverMessage", "vous etes déconnecté");
                                        serverLog("clients connectes: "+this.serveur.showConnectedClients());
                                    }else {
                                        this.sendResponse("serverMessage", "ERR Vous n'êtes pas connecté en tant que joueur pour "+this.clientInetAdress);
                                        serverLog("clients connectes: "+this.serveur.showConnectedClients());
                                    }
                                    break;

                                default:
                                    this.sendResponse("serverMessage","ERR commande non connue");
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
            System.err.println("Déconnexion client inattendu");
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.serveur.disconnect(this.clientInetAdress);
        }
    }
}
