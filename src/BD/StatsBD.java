import java.sql.Connection;
import java.sql.PreparedStatement;

import modele.Joueur;
import modele.Stats;

public class StatsBD {
    private ConnexionMySQL connexion;

    public StatsBD(){
        this.connexion = new ConnexionMySQL("servinfo-maria", "DBakhtar", "akhtar", "noalecaca");
    }

    public Stats getStatsparJoueur(Joueur joueur){
        try{
            Connection c = this.connexion.getConnexion();
            String requete = "SELECT NbVictoires, NbParties FROM STATS WHERE idp = ?";
            PreparedStatement ps = c.prepareStatement(requete);
            ps.setInt(1, joueur.getId())
        }
    }
}



