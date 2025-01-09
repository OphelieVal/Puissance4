
package modele;

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
    for (int i = 0; i<this.nbLignes; i++){
      for (int j = 0; j<this.nbColonnes; j++){
        this.lePlateau[i][j] = new Case(i, j);
      }
    }
  }

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
  public boolean poseJeton(int y, Joueur joueur) throws EnDehorsDuPlateauException, GagnantException {
    int x = this.getMaxligne(y);
    try {
      this.lePlateau[x][y].poseJeton(joueur.getCouleur());
    } 
    catch (Exception e) {
      throw new EnDehorsDuPlateauException("Le jeton n'a pas pu être ajouté");
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
  
}
