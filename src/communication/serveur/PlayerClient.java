package communication.serveur;

import java.util.List;
import modele.Joueur;
import modele.Plateau;

public class PlayerClient {
    private String nomJoueur;
    private String clientIP;
    private Joueur clientPlayer;
    private List<Plateau> listeMatchs;
    private int nbVictoires = 0;
    private double pourcentVict = 0;

    public PlayerClient(String nomJoueur, String clientIP) {
        this.nomJoueur = nomJoueur;
        this.clientIP = clientIP;
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
     * @param nomJoueur nom du joueur
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

    public List<Plateau> getListeMatchs() {
        return listeMatchs;
    }

    public void setListeMatchs(List<Plateau> listeMatchs) {
        this.listeMatchs = listeMatchs;
    }

    public int getNbVictoires() {
        return nbVictoires;
    }

    public void setNbVictoires(int nbVictoires) {
        this.nbVictoires = nbVictoires;
    }

    public double getPourcentVict() {
        return pourcentVict;
    }

    public void setPourcentVict(double pourcentVict) {
        this.pourcentVict = pourcentVict;
    }
  
    /**
     * update le nombre de victoires d'un client
     */
    public void updateVictoires() {
        this.nbVictoires += 1;
        this.pourcentVict = this.nbVictoires/this.listeMatchs.size();
    }

    public int getNbMatchs() {
        return this.listeMatchs.size();
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