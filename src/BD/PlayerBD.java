import modele.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import modele.Joueur;


public class PlayerBD {
    private ConnexionMySQL connexion;

    public PlayerBD(ConnexionMySQL connexion){ 
        this.connexion = new ConnexionMySQL("servinfo-maria", "DBakhtar", "akhtar", "noalecaca");
    }
    
    public List<Joueur> getListeJoueurs(){
        List<modele.Joueur> res = new ArrayList<>();
        try{
            Connection c = this.connexion.getConnexion();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select idp, nomp, couleurp from PLAYER");
            int idp;
            String nomp, couleurp;
            while(rs.next()){
                idp = rs.getInt("idp");
                nomp = rs.getString("nomp");
                couleurp = rs.getString("couleurp");
                Joueur joueur = new modele.Joueur(nomp, couleurp, idp);
                res.add(joueur);
            }
            rs.close();
        }catch(SQLException e){
            System.out.println("erreur :" + e.getMessage());
        }
        return res;
    }

    public List<Joueur> getListeJoueursEnLigne() {
        List<Joueur> res = new ArrayList<>();
        try {
            Connection c = this.connexion.getConnexion();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT idp, nomp, couleurp FROM PLAYER WHERE connecte = true");
            int idp;
            String nomp, couleurp;
            while (rs.next()) {
                idp = rs.getInt("idp");
                nomp = rs.getString("nomp");
                couleurp = rs.getString("couleurp");
                Joueur joueur = new Joueur(nomp, couleurp, idp);
                res.add(joueur);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
        return res;
    }
    
    public void setJoueurConnecte(int idJoueur, boolean connecte) {
        try {
            Connection c = this.connexion.getConnexion();
            PreparedStatement ps = c.prepareStatement("UPDATE PLAYER SET connecte =? WHERE idp =?");
            ps.setBoolean(1, connecte);
            ps.setInt(2, idJoueur);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
    }
}
