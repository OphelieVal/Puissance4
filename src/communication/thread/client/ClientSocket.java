package communication.thread.client;

import communication.client.Client;
import communication.client.ClientState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ClientSocket extends Thread {
    private final int port;
    private final String ip;
    private Socket socket;
    private final Client client;
    private InputStreamReader stream;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean isConnected = false;


    public ClientSocket(int listenPort, String ip, Client client) {
        this.port = listenPort;
        this.ip = ip;
        this.client = client;
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

    public void closeSocket() throws IOException {
        this.socket.close();
        this.reader.close();
        this.writer.close();
        this.interrupt();
    }

    public void handleSreverInstruction(String serverInstruction) throws InterruptedException {
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
                }
                break;
            default:
                System.out.println("no available: " + type);
                break;
        }
        Thread.sleep(1000);
    }

    @Override
    public void run() {
        try {
            clientSocketInit();
            while (true) {
                String serverMessage = this.reader.readLine();
                if (!serverMessage.isEmpty()) {
//                    System.out.println("global stmt: "+serverMessage);
                    String[] infos = serverMessage.split("\\|");
                    String indicator = infos[0].split(":")[1];
                    switch (indicator) {
                        case "clientInstruction":
                            this.handleSreverInstruction(infos[1]);
                            break;
                        case "serverMessage":
                            System.out.println("Message from server: " + infos[1]);
                            break;
                        default:
                            System.out.println("Unknown indicator: " + infos[1]);
                            break;
                    }
                }
            }
        } catch (Exception e) {
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