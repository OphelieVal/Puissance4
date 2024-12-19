package communication.serveur;

import modele.Joueur;

public class PlayerClient {
    private String username;
    private String clientIP;
    private Joueur clientPlayer;

    public PlayerClient(String username, String clientIP) {
        this.username = username;
        this.clientIP = clientIP;
    }

    /**
     * @return nom du joueur
     */
    public String getUsername() { 
        return username; 
    }
    public String getClientIP() { 
        return clientIP; 
    }

    public Joueur getClientPlayer() { 
        return clientPlayer; 
    }

    public void setClientPlayer(Joueur clientPlayer) { 
        this.clientPlayer = clientPlayer; 
    }

    public void setUsername(String username) {
         this.username = username; 
        }

    public void setClientIP(String clientIP) { 
        this.clientIP = clientIP; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof PlayerClient)) return false;
        PlayerClient newPlayer = (PlayerClient) o;
        return this.clientIP.equals(newPlayer.clientIP) && this.username.equals(newPlayer.getUsername());
    }

    @Override
    public String toString() {
        String ps = "Pas de couleur choisie";
        if (clientPlayer != null) {
            ps = this.clientPlayer.toString();
        }
        return "("+this.username+", "+this.clientIP+ ", "+ ps +")";
    }

    @Override
    public int hashCode() {
        return this.username.hashCode() + this.clientIP.hashCode();
    }
}
