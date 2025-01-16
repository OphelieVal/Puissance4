package communication.thread.server;

import communication.serveur.PlayerClient;
import communication.serveur.Serveur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import modele.*;

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

    public String getClientIP() {return this.clientInetAdress;}

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

    /** Procedure de demande de recherche de partie avec un autre joueur
     * @param args Commandes et argument
     */
    public void ask_server(String[] args){
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
    }

    /**
     *
     * @param args
     * @param status
     * @throws IOException
     */
    public void connect_server(String[] args, boolean status) throws IOException{
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
    }

    /**
     * recherche de joueur dans le serveur
     * @param args
     * @param status
     */
    public void isawait(String[] args, boolean status){
//        if (status) this.sendResponse("serverMessage", "LOOKING FOR ANOTHER PLAYER TO JOIN...");
//        else {
//            Plateau plateau = this.serveur.getClient(args[1]).getClientPlayer().getPlateau();
//            if (plateau.getTurn().equals(args[1])) {
//                this.sendResponse("clientInstruction", "SET INGAME STATE OK");
//            } else {
//                this.sendResponse("clientInstruction", "SET WAITGAME");
//                serverLog(args[1] + " mis en attente\n");
//            }
//        }
//        this.sendResponse("serverMessage", "GAME FOUND");
//        this.sendResponse("serverMessage", "STARTING...");
        if (!status) {
            Plateau plateau = this.serveur.getClient(args[1]).getClientPlayer().getPlateau();
            if (plateau.getTurn().equals(args[1])) {
                this.sendResponse("clientInstruction", "SET INGAME STATE OK");
            } else {
                this.sendResponse("clientInstruction", "SET WAITGAME");
                serverLog(args[1] + " mis en attente\n");
            }
            this.sendResponse("serverMessage", "GAME FOUND");
            this.sendResponse("serverMessage", "STARTING...");
        } else {
            this.serverLog("LOOKING FOR ANOTHER PLAYER TO JOIN...");
//            this.serverLog("LOOKING FOR ANOTHER PLAYER TO JOIN...");
//            this.serverLog("LOOKING FOR ANOTHER PLAYER TO JOIN...");
//            this.sendResponse("serverMessage", "LOOKING FOR ANOTHER PLAYER TO JOIN...");
//            this.sendResponse("serverMessage", "GAME FOUND");
//            this.sendResponse("serverMessage", "STARTING...");
        }

    }

    public void handle() throws IOException {
        String message = reader.readLine();
        if (message==null){
            return;
        }
        String[] args = message.split(" ");
        if (!message.isEmpty()) {
            this.serverLog("received interaction: [" + message + "] from client: " + this.clientInetAdress +
                    " | command and args numbers: " + args.length);
            boolean status;
            String indicator = args[0];
            switch (indicator.toLowerCase()) {

                case "connect":
                    serverLog("received connection request from: " + this.clientInetAdress + " to username: " + args[1]);
                    status = this.serveur.clientIsConnected(args[1], this.clientInetAdress);
                    this.connect_server(args, status);
                    break;

                case "ask":
                    this.ask_server(args);
                    break;

                case "isawait":
                    status = this.serveur.isPlayerInAwaitingQueue(args[1], this.clientInetAdress);
                    this.isawait(args, status);
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

                case "play":
                    String player = args[2];
                    Integer colonne = Integer.parseInt(args[1]);
                    Plateau plateau = this.serveur.getClient(player).getClientPlayer().getPlateau();
                    if (plateau.getTurn().equals(player) && !(plateau.isGameEnded())) {
                        String result = this.serveur.play(colonne, player);
                        this.sendResponse("serverMessage", result);
                        serverLog(player + " a joué colonne " + colonne + " resultat plateau : " + result);
                        if (plateau.getJoueur1().getNomJoueur().equals(player)){
                            plateau.setTurn(plateau.getJoueur2().getNomJoueur());
                        }
                        else {
                            plateau.setTurn(plateau.getJoueur1().getNomJoueur());
                        }

                        this.sendResponse("clientInstruction", "SET WAITGAME");
                    }
                    else {
                        this.sendResponse("serverMessage", player + " a tenté de jouer");
                    }
                    if (plateau.getJoueur1()==null && plateau.getJoueur2()==null){
                        this.sendResponse("clientInstruction", "SET USERCONNECTED");
                            serverLog("fin partie "+ player);
                    }
                    break;

                case "waitgame":
                    String joueur = args[1];
                    String showInTerninal = args[2];
                    plateau = this.serveur.getClient(joueur).getClientPlayer().getPlateau();
                    if (plateau.isGameEnded()) {
                        this.sendResponse("serverMessage", this.serveur.getInGamePlateau(joueur));
                        this.sendResponse("serverMessage", "GAME ENDED");
                        this.sendResponse("serverMessage", "Le gagnant est: "+plateau.getWinner().getNomJoueur());
                        this.sendResponse("clientInstruction", "SHOW WINNER "+plateau.getWinner().getNomJoueur());
                        this.sendResponse("clientInstruction", "SET GAMEENDED");
                    }else if (plateau.getTurn().equals(joueur) && !(plateau.isGameEnded())){
                        this.sendResponse("clientInstruction", "SET INGAME");
                        serverLog("au tour de " + joueur);
                    }else {
                        this.serverLog("au tour de " + plateau.getTurn());
                        if (showInTerninal.equals("true")) this.sendResponse("serverMessage", "En attente de votre tour");
                    }
                    break;

                case "getactualplate":
                    System.out.println("getactualplate: " + args[1]);
                    serverLog("player: " + args[1] + " asked to see the actual game plate");
                    this.sendResponse("serverMessage", this.serveur.getInGamePlateau(args[1]));
                    break;

                case "home":
                    serverLog("client: " + this.clientInetAdress + " went to the main menu");
                    Joueur currentPlayer   = this.serveur.getClient(args[1]).getClientPlayer();
                    currentPlayer.setInitialState();
                    this.sendResponse("serverMessage","redirecting to home menu");
                    this.sendResponse("clientInstruction", "SET USERCONNECTED STATE OK");
                    break;

                case "playerstats":
                    serverLog("client: " + this.clientInetAdress + " asked to see the stats of: " + args[1]);
                    Stats stats = this.serveur.getstatsBd().getStatsparJoueur(this.serveur.getClient(args[1]).getClientPlayer());
                    this.sendResponse("serverMessage", "Vous possédez " + stats.getNbParties() + " parties totales et " + stats.getNbVictoires() + " victoires.");
                    break;

                case "playerslist":
                    serverLog("client: " + this.clientInetAdress + " asked to see the list of connected players");
                    this.sendResponse("serverMessage", "Functionality not implemented yet (See connected players)");
                    // TODO fonction pour voir la list de joueur en ligne
                    break;

                case "allplayerslist":
                    serverLog("client: " + this.clientInetAdress + " asked to see the list of all registered players");
                    this.sendResponse("serverMessage", "Functionality not implemented yet (See registered players)");
                    // TODO fonction pour voir la liste de joueur possedant des stats sur Puissance-4
                    break;

                default:
                    this.sendResponse("serverMessage","ERR commande non connue");
                    break;
            }
        }
    }

    @Override
    public void run() {
        String message = "";
        try {
            while (!socket.isClosed()) {
                try {
                    this.handle();
                } catch (SocketException e ) {
                    serverLog("Client socket error: "+ this.clientInetAdress);
                    break;
                }
            }
        } catch (Exception e) {
            serverLog("Déconnexion client inattendu : " + this.clientInetAdress);

//            try {
//                this.serveur.forceEndGame(this.clientInetAdress);
//
//            } catch (InGamePLayerDisconnected ex) {
//                throw new RuntimeException(ex);
//            }
            System.err.println(e.getMessage() + e.getCause()); //debug
        }
        finally {
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
