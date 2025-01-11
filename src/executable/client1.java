package executable;

import communication.client.Client;
import java.io.IOException;

public class client1 {
    public static void main(String[] args) throws IOException {
        System.out.println("Client 1 actif");
        String server = "192.168.46.108";
        int port = 4444;
        Client client = new Client(server, port);
        System.out.println(client.get_ClientIP());
        client.start();
    }
}
