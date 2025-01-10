package executable;

import communication.client.Client;

import java.io.IOException;

public class client2 {
    public static void main(String[] args) throws IOException {
        System.out.println("Client 2 actif");
        String server = "192.168.1.200";
        int port = 4444;
        Client client = new Client(server, port);
        System.out.println(client.get_ClientIP());
        client.execute();
    }
}
