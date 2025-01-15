package bd;
import java.sql.*;
import java.sql.SQLException;

import modele.Joueur;
import modele.Stats;

public class StatsBD {
    private ConnexionMySQL connexion;

    public StatsBD(){
        this.connexion = new ConnexionMySQL("servinfo-maria", "DBakhtar", "akhtar", "noalecaca");
    }

    public Stats getStatsparJoueur(Joueur joueur){
        Stats stats = new Stats(null,0,0);
        try{
            Connection c = this.connexion.getConnexion();
            String requete = "SELECT NbVictoires, NbParties FROM STATS WHERE pseudo = ?";
            PreparedStatement ps = c.prepareStatement(requete);
            ps.setString(1, joueur.getNomJoueur());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                int nbVictoires = rs.getInt("NbVictoires");
                int nbParties = rs.getInt("NbParties");
                stats = new Stats(joueur, nbVictoires, nbParties);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
        return stats;
    }

    public void ajouterStats(Joueur joueur, int nbVictoires, int nbParties){
        try{
            Connection c = this.connexion.getConnexion();
            String requete = "INSERT INTO STATS (pseudo, NbVictoires, NbParties) VALUES (?,?,?)";
            PreparedStatement ps = c.prepareStatement(requete);
            ps.setString(1, joueur.getNomJoueur());
            ps.setInt(2, nbVictoires);
            ps.setInt(3, nbParties);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
    }

    public void modifierStats(Joueur joueur, int nbVictoires, int nbParties){
        try{
            Connection c = this.connexion.getConnexion();
            String requete = "UPDATE STATS SET NbVictoires =?, NbParties =? WHERE pseudo =?";
            PreparedStatement ps = c.prepareStatement(requete);
            ps.setInt(1, nbVictoires);
            ps.setInt(2, nbParties);
            ps.setString(3, joueur.getNomJoueur());
            ps.executeUpdate();
            ps.close();
        }
        catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
    }

    public void incrementStats(Joueur joueur, boolean victoire){
        try{
            Connection c = this.connexion.getConnexion();
            Stats actuelStats = this.getStatsparJoueur(joueur);
            String requete = "UPDATE STATS SET NbVictoires =?, NbParties =? WHERE pseudo =?";
            PreparedStatement ps = c.prepareStatement(requete);
            ps.setInt(1, actuelStats.getNbVictoires()+1);
            ps.setInt(2, actuelStats.getNbParties()+1);
            ps.setString(3, joueur.getNomJoueur());
            ps.executeUpdate();
            ps.close();
        }
        catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
    }

    public void supprimerStats(Joueur joueur){
        try{
            Connection c = this.connexion.getConnexion();
            String requete = "DELETE FROM STATS WHERE pseudo =?";
            PreparedStatement ps = c.prepareStatement(requete);
            ps.setString(1, joueur.getNomJoueur());
            ps.executeUpdate();
            ps.close();
        }
        catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
    }
}



