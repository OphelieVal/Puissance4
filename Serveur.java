import java.util.List;
import java.util.ArrayList;

class Serveur {
  private List<Plateau> lesPlateaux;
  private List<Joueur> lesJoueurs;

  public Serveur(){
    this.lesJoueurs = new ArrayList<>();
    this.lesPlateaux = new ArrayList<>();
  }
  public void ajouter_plateau(Plateau plateau){}
  
  public void  connect(String nomJoueur) {
  }

  public String wait(String nomJoueur) {
  }

  public String ask(String nomJoueur) {
  }

  public String play(int nomColonne) {
  }

  public String update(String nomJoueur) {
  }

  public boolean win(String nomJoueur) {
  }

  public String end(String nomJoueur) {
  }

  public String getStats(String nomJoueur) {
  }

  public String quit(String nomJoueur) {
  }

}
