
import java.util.ArrayList;
import java.util.List;

class Client {
  private String IP;
  private List<Plateau> listeMatchs;
  private String nomJoueur;
  private int nbVictoires = 0;
  private double pourcentVict = 0;

  public  Client(String IP, String nomJoueur) {
    this.IP = IP;
    this.nomJoueur = nomJoueur;
    this.listeMatchs = new ArrayList<>();
  }

  public void updateVictoires() {
    this.nbVictoires += 1;
    this.pourcentVict = this.nbVictoires/this.listeMatchs.size();
  }

  public String get_IP(){
    return this.IP;
  }

  public String get_nomJoueur(){
    return this.nomJoueur;
  }

}
