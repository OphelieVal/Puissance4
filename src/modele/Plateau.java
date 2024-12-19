
package modele;
import java.util.List;

public class Plateau {
  private int nbLignes = 0;

  private int nbColonnes = 0;

  private int casesRestantes;

  private Case[][] lePlateau;


  public Plateau(int nbLignes, int nbColonnes) {
    this.nbLignes = nbLignes;
    this.nbColonnes = nbColonnes;
    this.casesRestantes = this.nbLignes * this.nbColonnes;
    this.lePlateau = new Case[this.nbLignes][this.nbColonnes];
  }

  public boolean poseJeton(int x, int y) throws IndexOutOfBoundsException {
    try {
      this.lePlateau[x][y].poseJeton(null);
    } 
    
    catch (Exception e) {
      System.err.println("Le jeton n'a pas pu être ajouté");
      throw new IndexOutOfBoundsException();

    }
    
    if (!(this.lePlateau[x][y].contientJeton())) {
      return false;
    }

    return true;
    
  }

  public void reset() {
    this.lePlateau = new Case[this.nbLignes][this.nbColonnes];
    this.casesRestantes = this.nbLignes * this.nbColonnes;
  }

  public Case getCase(int numLigne, int numColonne) throws IndexOutOfBoundsException {
    try {
      Case element = this.lePlateau[numLigne][numColonne];
      return element;
    } 
    
    catch (Exception e) {
      System.err.println("la case renseignée n'existe pas");
      throw new IndexOutOfBoundsException();
    }
  }

    public Case[][] getLePlateau() {
        return lePlateau;
    }

    public void setLePlateau(Case[][] lePlateau) {
        this.lePlateau = lePlateau;
    }
  
}
