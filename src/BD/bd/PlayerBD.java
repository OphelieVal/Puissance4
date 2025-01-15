package bd;
import modele.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PlayerBD {
    private ConnexionMySQL connexion;

    public PlayerBD(){ 
        this.connexion = new ConnexionMySQL("servinfo-maria", "DBakhtar", "akhtar", "noalecaca");
    }
    
    public List<Joueur> getListeJoueurs(){
        List<modele.Joueur> res = new ArrayList<>();
        try{
            Connection c = this.connexion.getConnexion();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select nomp, couleurp from PLAYER");
            String nomp, couleurp;
            while(rs.next()){
                nomp = rs.getString("nomp");
                couleurp = rs.getString("couleurp");
                Joueur joueur = new modele.Joueur(nomp, couleurp);
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
            ResultSet rs = s.executeQuery("SELECT nomp, couleurp FROM PLAYER WHERE connecte = true");
            String nomp, couleurp;
            while (rs.next()) {
                nomp = rs.getString("nomp");
                couleurp = rs.getString("couleurp");
                Joueur joueur = new Joueur(nomp, couleurp);
                res.add(joueur);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
        return res;
    }
    
    public void setJoueurConnecte(String pseudo, boolean connecte) {
        try {
            Connection c = this.connexion.getConnexion();
            PreparedStatement ps = c.prepareStatement("UPDATE PLAYER SET connecte =? WHERE pseudo =?");
            ps.setBoolean(1, connecte);
            ps.setString(2, pseudo);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("erreur : " + e.getMessage());
        }
    }
}
