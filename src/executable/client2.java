package executable;

import communication.client.Client;

import java.io.IOException;

public class client2 {
    public static void main(String[] args) throws IOException {
        boolean logs = false;
        System.out.println("Client 2 actif");
        String server = "localhost";
        int port = 4444;
        Client client = new Client(server, port, logs);
        System.out.println(client.get_ClientIP());
        client.start();
        System.out.println("Connexion au server...");
    }
}
