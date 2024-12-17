
import java.util.List;
import java.util.ArrayList;

class Serveur {
  private List<Plateau> lesPlateaux;
  private List<Joueur> lesJoueurs;
  private int port;

  public Serveur(int port){
    this.lesJoueurs = new ArrayList<>();
    this.lesPlateaux = new ArrayList<>();
    this.port = port;
  }
  public int get_port(){
    return this.port;
  }
  public void ajouter_plateau(Plateau plateau){
    this.lesPlateaux.add(plateau);
  }
  public void connect(String nomJoueur) {
    // ajouter un joueur a la liste joueur
  }

  public String wait(String nomJoueur) {
    return "";
  }

  public String ask(String nomJoueur) {
    return "";
  }

  public String play(int nomColonne) {
    return "";
  }

  public String update(String nomJoueur) {
    return "";
  }

  public boolean win(String nomJoueur) {
    return false;
  }

  public String end(String nomJoueur) {
    return "";
  }

  public String getStats(String nomJoueur) {
    return "";
  }

  public String quit(String nomJoueur) {
    return "";

  }
}
