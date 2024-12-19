
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

  /** pose un jeton sur une case du plateau 
   * @param x la ligne du plateau
   * @param y la colonne du plateau
   * @return true si le jeton s'est posé sinon false
   * @throws IndexOutOfBoundsException en dehors du plateau
   */
  public boolean poseJeton(int x, int y) throws EnDehorsDuPlateauException {
    try {
      this.lePlateau[x][y].poseJeton(null);
    } 
    catch (Exception e) {
      throw new EnDehorsDuPlateauException("Le jeton n'a pas pu être ajouté");
    }
    if (!(this.lePlateau[x][y].contientJeton())) {
      return false;
    }
    return true;
  }

  /** reset l'ensemble des cases du plateau
   * 
   */
  public void reset() {
    this.lePlateau = new Case[this.nbLignes][this.nbColonnes];
    this.casesRestantes = this.nbLignes * this.nbColonnes;
  }

  /** getter d'une case du plateau
   * @param numLigne ligne de la case
   * @param numColonne colonne de la case
   * @return case du plateau 
   * @throws IndexOutOfBoundsException
   */
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

    /** gette cases du plateau
     * @return l'ensemble des cases du plateau
     */
    public Case[][] getLePlateau() {
        return lePlateau;
    }

    /** setter case plateau
     * @param lePlateau set les cases du plateau
     */
    public void setLePlateau(Case[][] lePlateau) {
        this.lePlateau = lePlateau;
    }
  
}
