package communication.thread.client;

import communication.client.Client;
import communication.client.ClientState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

    @Override
    public void run() {
        try {
            clientSocketInit();
            while (true) {
                String serverMessage = this.reader.readLine();
                if (!serverMessage.isEmpty()) {
                    String[] infos = serverMessage.split(" ");
                    if (serverMessage.equals("exit")) {
                        this.client.set_ClientState(ClientState.USERDISCONNECTED);
                        break;
                    }else if(infos[0].equals("connecté") && infos[2].equals("OK")) {
                        this.client.set_ClientState(ClientState.USERCONNECTED);
                    }
                    System.out.println("Message from server: " + serverMessage);
                }
            }
            this.closeSocket();
            System.out.print("Quitting");
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