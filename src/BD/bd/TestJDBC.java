package BD.bd;
import java.sql.Connection;

public class TestJDBC{
    public ConnexionMySQL connexion;

    public static void main ( String [] args ) {
        String loginMySQL = "bocquet"; 
        String mdpMySQL = "bocquet";   
        String nomServeur = "servinfo-maria"; 
        String nomBase = "DBbocquet"; 

        ConnexionMySQL con = new ConnexionMySQL(nomServeur, nomBase, loginMySQL, mdpMySQL);

        if (con.getConnecte()) {
            System.out.println("La connexion s'est bien passée."); 
            Connection connex = con.getConnexion();
        } 
        else {
            System.out.println("La connexion à votre BD ne s'est pas faite.");
        }
    }
}
