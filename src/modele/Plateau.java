
package modele;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Plateau {
  private int nbLignes = 0;

  private int nbColonnes = 0;

  private int casesRestantes;

  private Case[][] lePlateau;
  private String turn;

  private List<String> allColors = Arrays.asList("YELLOW", "RED", "BLUE", "PURPLE");
  private List<String> chosenColors = new ArrayList<>();
  private Joueur j1;
  private Joueur j2;

  public Plateau(int nbLignes, int nbColonnes) {
    this.nbLignes = nbLignes;
    this.nbColonnes = nbColonnes;
    this.casesRestantes = this.nbLignes * this.nbColonnes;
    this.lePlateau = new Case[this.nbLignes][this.nbColonnes];
    for (int i = 0; i<this.nbLignes; i++){
      for (int j = 0; j<this.nbColonnes; j++){
        this.lePlateau[i][j] = new Case(i, j);
      }
    }
    this.j1 = j1;
    this.j2 = j2;
  }

    public Joueur getJoueur1(){
      return this.j1;
    }
  public Joueur getJoueur2(){
    return this.j2;
  }

  public String getTurn(){
    return this.turn;
  }

  public void setTurn(String turn){
    this.turn = turn;
  }

  public String getAColor() {
    for (String color : this.allColors) {
      if (!this.chosenColors.contains(color)) {
        return color;
      }
    }
    return null;
  }

  public void setChosenColors(List<String> chosenColors) {
    this.chosenColors = chosenColors;
  }

  /** pose un jeton sur une case du plateau
  /**
   * get la position x libre de la colonne y
   * @param y
   * @return la ligne disponible pour la colonne y
   */
  public Integer getMaxligne(int y){
    for (int x = 0; x<this.nbLignes; x++){
      if (!(this.lePlateau[x][y].contientJeton())){
        return x;
      }
    }
    return null;


  }

  /** pose un jeton sur une case du plateau 
   * @param x la ligne du plateau
   * @param y la colonne du plateau
   * @return true si le jeton s'est posé sinon false
   * @throws IndexOutOfBoundsException en dehors du plateau
   */
  public boolean poseJeton(int y, Joueur joueur) throws EnDehorsDuPlateauException, GagnantException, OccupeeException {
    Integer x = this.getMaxligne(y);
    if (x==null){
      throw new EnDehorsDuPlateauException("Cette colonne est déjà remplie");
    }
    try {
      if (joueur.aGagne(x, y)){
        throw new GagnantException();
      }
      this.lePlateau[x][y].poseJeton(joueur.getCouleur());
    } 
    catch (OccupeeException e){
      throw new OccupeeException(e.getMessage());
    }
    catch (Exception e) {
      throw new EnDehorsDuPlateauException("Le jeton doit être posé dans la colonne comprise entre " + 0 + " et " + this.nbColonnes);
    }
    if (!(this.lePlateau[x][y].contientJeton())) {
      return false;
    }
    if (joueur.aGagne(x, y)){
      throw new GagnantException();
    }
    this.casesRestantes--;
    return true;
  }

  /** reset l'ensemble des cases du plateau
   *
   */
  public void reset() {
    for (int i = 0; i<this.nbLignes;i++){
      for (int j = 0; j<this.nbColonnes;j++){
        this.lePlateau[i][j].reset();
      }
    }
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

    /** getter cases du plateau
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

    /**
     * getter nb lignes du plateau
     * @return nombre lignes du plateau
     */
    public int getNbLignes(){
      return this.nbLignes;
    }

    /**
     * getter nb colonnes du plateau
     * @return nombre colonnes du plateau
     */
    public int getNbColonnes(){
      return this.nbColonnes;
    }

    public int getCasesRestantes(){
      return
       this.casesRestantes;
    }

    /**
     * verifie si le joueur a gagné verticalement
     * @param casedepart
     * @return nb de pions alignés
     */
    public int quatreVertical(Case casedepart) {
      int ligne = casedepart.getLigne();
      int colonne = casedepart.getColonne();
      String couleurJeton = casedepart.getCouleur();
      int cpt = 1;
      for (int i = ligne - 1; i >= 0; i--) {
          if (lePlateau[i][colonne].getCouleur() != null && lePlateau[i][colonne].getCouleur().equals(couleurJeton)) {
              cpt++;
              if (cpt >= 4) {
                  return cpt;
              }
          } else {
              break;
          }
      }
      for (int i = ligne + 1; i < lePlateau.length; i++) {
          if (lePlateau[i][colonne].getCouleur() != null && lePlateau[i][colonne].getCouleur().equals(couleurJeton)) {
              cpt++;
              if (cpt >= 4) {
                  return cpt;
              }
          } else {
              break;
          }
      }
      return cpt;
  }

  /**
     * verifie si le joueur a gagné horizontalement
     * @param casedepart
     * @return nb de pions alignés
     */
  public int quatreHorizontal(Case casedepart) {
    int ligne = casedepart.getLigne();
    int colonne = casedepart.getColonne();
    String couleurJeton = casedepart.getCouleur();
    int cpt = 1;
    for (int j = colonne - 1; j >= 0; j--) {
        if (lePlateau[ligne][j].getCouleur() != null && lePlateau[ligne][j].getCouleur().equals(couleurJeton)) {
            cpt++;
            if (cpt >= 4) {
                return cpt;
            }} else {
            break;
        }
    }
    for (int j = colonne + 1; j < lePlateau[0].length; j++) {
        if (lePlateau[ligne][j].getCouleur() != null && lePlateau[ligne][j].getCouleur().equals(couleurJeton)) {
            cpt++;
            if (cpt >= 4) {
                return cpt;
            }
        } else {
            break;
        }}
    return cpt;}

    /**
     * verifie si le joueur a gagné diagonalement
     * @param casedepart
     * @return nb de pions alignés
     */
    public int quatreDiagonal(Case casedepart) {
      int ligne = casedepart.getLigne();
      int colonne = casedepart.getColonne();
      String couleurJeton = casedepart.getCouleur();
      int cpt = 1;
      for (int i = ligne + 1, j = colonne - 1; i < lePlateau.length && j >= 0; i++, j--) {
          if (lePlateau[i][j].getCouleur() != null && lePlateau[i][j].getCouleur().equals(couleurJeton)) {
              cpt++;
              if (cpt >= 4) {
                  return cpt;
              }
          } else {
              break;
          }
      }
      for (int i = ligne - 1, j = colonne + 1; i >= 0 && j < lePlateau[0].length; i--, j++) {
          if (lePlateau[i][j].getCouleur() != null && lePlateau[i][j].getCouleur().equals(couleurJeton)) {
              cpt++;
              if (cpt >= 4) {
                  return cpt;
              }
          } else {
              break;
          }
      }
      return cpt;}


    public String getVisualPlate() {
      StringBuilder plate = new StringBuilder();
      plate.append("VALUE-");
      for (int i = this.nbLignes-1; i>=0; i--){
        plate.append("LINESEP[ ");
        for (int j = 0; j<this.nbColonnes; j++){
            String currentCase = " ( " ;
            currentCase += this.getCase(i, j).getCouleur();
            currentCase += " ) ";
            if (j != this.nbColonnes-1){
              currentCase += " ";
            }
            plate.append(currentCase);
        }
        plate.append(" ] ");
      }
      return plate.toString();
    }

    @Override
    public String toString() {
      if (this.getVisualPlate() != null) return "PLATEAU_VISUAL_STRING-" + this.getVisualPlate();
      else return "no plate to show";
    }

    @Override
    public boolean equals(Object objet){
      if (objet == null){return false;}
      if (objet == this){return true;}
      if (!(objet instanceof Case)){return false;}
      Plateau tmp = (Plateau) objet;
      if (this.nbLignes == tmp.getNbLignes() && this.nbColonnes == tmp.getNbColonnes()){
          if (this.casesRestantes == tmp.getCasesRestantes()){
              if (this.lePlateau.equals(tmp.getLePlateau())){return true;}
          }
      }
      return false;
  }

  @Override
  public int hashCode(){
    int hash = this.nbLignes + this.nbColonnes + this.casesRestantes + this.turn.hashCode() * 97;
    return hash;

  }

}
