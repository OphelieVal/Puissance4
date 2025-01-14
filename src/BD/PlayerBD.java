import modele.Joueur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerBD {
    private ConnexionMySQL connexion;

    public PlayerBD(ConnexionMySQL connexion){ 
        this.connexion = new ConnexionMySQL("servinfo-maria", "DBakhtar", "akhtar", "noalecaca");
    }
    
    public List<Joueur> getListePersonne(){
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
}
