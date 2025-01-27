package communication.thread.client;

import communication.client.Client;
import communication.client.ClientState;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientSocket extends Thread {
    private final int port;
    private final String ip;
    private Socket socket;
    private final Client client;
    private InputStreamReader stream;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean isConnected = false;
    private boolean showLogs = false;


    public ClientSocket(int listenPort, String ip, Client client) {
        this.port = listenPort;
        this.ip = ip;
        this.client = client;
    }

    public ClientSocket(int listenPort, String ip, Client client, boolean showLogs) {
        this.port = listenPort;
        this.ip = ip;
        this.client = client;
        this.showLogs = showLogs;
    }

    public void clientSocketInit() throws IOException {
            this.socket = new Socket(this.ip, this.port);
            this.stream = new InputStreamReader(this.socket.getInputStream());
            this.reader = new BufferedReader(stream);
            this.writer = new PrintWriter(socket.getOutputStream());
            this.isConnected = true;
    }

    public void sendCommand(String command) throws IOException {
        if (!this.isConnected) {
            System.out.println("Client not connected, please init client connection first");
        }
        this.writer.println(command);
        this.writer.flush(); //on peut aussi mettre le autoFlush (mais a eviter)
    }

    /**
     * Permer d'afficher un log dans la console du client
     * forme [yyyy-MM-dd HH:mm:ss] + message
     * @param message message du log
     */
    public void clientLog(String message) {
        if (this.showLogs) {
            String timeStamp = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(Calendar.getInstance().getTime());
            System.out.println(timeStamp + " : "+ message);
        }
    }

    public void closeSocket() throws IOException {
        this.socket.close();
        this.reader.close();
        this.writer.close();
        this.interrupt();
    }

    /**
     * Execute les instruction donne par le server de jeu
     * @param serverInstruction instruction complete du serveur
     * @throws InterruptedException Exception
     */
    public void handleServerInstruction(String serverInstruction) throws InterruptedException, IOException {
        if (this.showLogs) this.clientLog("get instruction: " + serverInstruction);
        String[] args = serverInstruction.split(" ");
        String type = args[0].toLowerCase();
        switch (type) {
            case "set":
                switch (args[1].toUpperCase()) {
                    case "USERCONNECTED":
                        this.client.set_ClientState(ClientState.USERCONNECTED);
                        break;
                    case "USERDISCONNECTED":
                        this.client.set_ClientState(ClientState.USERDISCONNECTED);
                        break;
                    case "LOOKINGADVERSARY":
                        this.client.set_ClientState(ClientState.LOOKINGADVERSARY);
                        break;
                    case "INGAME":
                        this.client.set_ClientState(ClientState.INGAME);
                        break;
                    case "WAITGAME":
                        this.client.set_ClientState(ClientState.WAITGAME);
                        break;
                    case "GAMEENDED":
                        this.client.set_ClientState(ClientState.ENDGAME);
                }
                break;

            case "initusername":
                String name = args[1].toUpperCase();
                if (name.equals("NULL")) this.client.setNomJoueur(null);
                else this.client.setNomJoueur(args[1]);
                break;
            case "NOTTURN":
                System.out.println("Ce n'est pas votre tour");

            case "SHOW":
                if (args[1].equals("WINER")) {
                    if (args[2].equals(this.client.get_nomJoueur())) System.out.println("Vous avez gagne la partie");
                    else System.out.println("Vous avez gagne la partie");
                }
                break;

            default:
                System.out.println("non disponible: " + type);
                break;
        }
//        Thread.sleep(1000);
    }

    @Override
    public void run() {
        try {
            clientSocketInit();
            while (true) {
                String serverMessage = this.reader.readLine();
                if ((serverMessage!=null)&&!serverMessage.isEmpty()) {
                    if (this.showLogs) this.clientLog("received interaction: [" + serverMessage + "] du serveur");
                    String[] infos = serverMessage.split("\\|");
                    String indicator = infos[0].split(":")[1];
                    switch (indicator) {
                        case "clientInstruction":
                            this.handleServerInstruction(infos[1]);
                            break;
                        case "serverMessage":
                            String[] temp = infos[1].split("-");
                            if (temp[0].equals("PLATEAU_VISUAL_STRING")) {
                                String[] lines = temp[2].split("LINESEP");
                                for (String line: lines) {
                                    System.out.println(line);
                                }
                            }else {
                                System.out.println("Message du serveur: " + infos[1]);
                            }
                            break;
                        default:
                            System.out.println("Indication non connue: " + infos[1]);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
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