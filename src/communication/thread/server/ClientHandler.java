package communication.thread.server;

import communication.serveur.Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private InputStreamReader stream;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private Serveur serveur;

    public ClientHandler(Socket socket, Serveur server) throws IOException {
        this.serveur = server;
        this.socket = socket;
        this.stream = new InputStreamReader(this.socket.getInputStream());
        this.reader = new BufferedReader(stream);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
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
                                    System.out.println(args[1]+ args[2]);
                                    status = this.serveur.clientIsConnected(args[1], args[2]);
                                    if (status) {
                                        this.writer.println("Already connected as "+args[1]+" OK");
                                        System.out.println("connected clients: "+this.serveur.showConnectedClients());
                                    }else if (this.serveur.connect(args[1], args[2])) {
                                        this.writer.println("connection confirmed as "+args[1]+" OK");
                                        System.out.println("connected clients: "+this.serveur.showConnectedClients());
                                    }else {
                                        this.writer.println("ERR connection to "+args[1]+" failed or refused");
                                    }

                                case "ask":
                                    String adversary = args[1];
                                    this.sendMessage("ask request received for "+adversary+ "but feature not implemented yet");
                                    break;
                                case "disconnect":
                                    status = this.serveur.disconnect(args[1]);
                                    if (status) {
                                        this.writer.println("disconnected, you are not a player anymore");
                                        System.out.println("remain connected clients: "+this.serveur.showConnectedClients());
                                    }else {
                                        this.writer.println("ERR disconnected didn't success for "+args[1]);
                                    }
                                default:
                                    this.sendMessage("ERR Unknown command");
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
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
