package executable;


import communication.serveur.Serveur;

import java.io.IOException;

public class server {
    public static void main(String[] args) throws IOException {
        int defaultPort = 4444; // port d'écoute 
        Serveur server = new Serveur(defaultPort);
        System.out.println("Serveur actif ");
    }
}
