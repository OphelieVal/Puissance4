package communication.serveur;

import java.util.Random;
import modele.Joueur;

public class PlayerClient {
    private String nomJoueur;
    private String clientIP;
    private Joueur clientPlayer;

    public PlayerClient(String nomJoueur, String clientIP) {
        this.nomJoueur = nomJoueur;
        this.clientIP = clientIP;
        Random rand = new Random();
        this.clientPlayer = new Joueur(nomJoueur, "rouge");
    }

    /** getter nom joueur
     * @return nom du joueur
     */
    public String getNomJoueur() { 
        return this.nomJoueur; 
    }

    /**getter IP client
     * @return IP du client
     */
    public String getClientIP() { 
        return this.clientIP; 
    }

    /** gette objet joueur
     * @return objet joueur
     */
    public Joueur getClientPlayer() { 
        return this.clientPlayer; 
    }

    /** setter joueur
     * @param clientPlayer joueur
     */
    public void setClientPlayer(Joueur clientPlayer) { 
        this.clientPlayer = clientPlayer;
    }

    /**setter nom joueur
     * @param nomJoueur nom du joueurs
     */
    public void setNomJoueur(String nomJoueur) {
         this.nomJoueur = nomJoueur; 
         this.clientPlayer.setNomJoueur(nomJoueur);
        }

    /** setter IP client
     * @param clientIP IP du client
     */
    public void setClientIP(String clientIP) { 
        this.clientIP = clientIP; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof PlayerClient)) return false;
        PlayerClient newPlayer = (PlayerClient) o;
        return this.clientIP.equals(newPlayer.clientIP) && this.nomJoueur.equals(newPlayer.getNomJoueur());
    }

    @Override
    public String toString() {
        String ps = "Pas de couleur choisie";
        if (clientPlayer != null) {
            ps = this.clientPlayer.toString();
        }
        return "("+this.nomJoueur+", "+this.clientIP+ ", "+ ps +")";
    }

    @Override
    public int hashCode() {
        return this.nomJoueur.hashCode() + this.clientIP.hashCode();
    }
}
