package modele;

import java.util.ArrayList;
import java.util.List;

public class Case {
  private boolean aJeton = false;
  private int x;
  private int y;
  private String couleur = null;
  private List<Case> lesVoisines;

  public Case(int x,int y){
    this.lesVoisines = new ArrayList<>();
    this.x = x;
    this.y = y;
  }

  /** remet par défaut la case
   */
  public void reset(){
    this.aJeton = false;
    this.couleur = null;
  }

  /** getter jeton case 
   * @return true si il y a un jeton
   */
  public boolean contientJeton(){
    return this.aJeton;
  }

  /** pose le jeton sur une case
   * @param couleur couleur du joueur
   * @return true si le jeton est posé
   * @throws Exception si la case contient deja un jeton
   */
  public boolean poseJeton(String couleur) throws Exception{
    if (!(this.contientJeton())){
      throw new Exception();
    }
    this.aJeton = true;
    this.couleur = couleur;
    return true;
  }

  /**
   * getter des cases voisines de la case actuelle
   * @return cases voisines
   */
  public List<Case> getlesVoisines(){
    return this.lesVoisines;
  }

  /**
   * getter couleur dans la case
   * @return couleur
   */
  public String getCouleur(){
    return this.couleur;
  }

  /**
   * getter ligne de placement de la case
   * @return ligne placement
   */
  public int getLigne(){
    return this.x;
  }

  /**
   * getter colonne de placement de la case
   * @return colonne placement
   */
  public int getColonne(){
    return this.y;
  }

  /**
   * verifie si quatre pions sont alignés horizontalement
   * @param casedepart
   * @return le nombre de pions alignés 
   */
  public int quatreHorizontal(Case casedepart) {
    String couleurJeton = this.getCouleur();
    int cpt = 1;
    for (Case case_voisine : this.lesVoisines){
      if (cpt>=4){
        return cpt;
      }
      if (!(case_voisine==casedepart)&&case_voisine.getColonne()==this.getColonne()&&case_voisine.getCouleur()==couleurJeton){
        cpt += case_voisine.quatreHorizontal(this);
      }
    }
    return cpt;
  }

  /**
   * verifie si quatre pions sont alignés verticalement
   * @param casedepart
   * @return le nombre de pions alignés 
   */
  public int quatreVertical(Case casedepart) {
    String couleurJeton = this.getCouleur();
    int cpt = 1;
    for (Case case_voisine : this.lesVoisines){
      if (cpt>=4){
        return cpt;
      }
      if (!(case_voisine==casedepart)&&case_voisine.getLigne()==this.getColonne()&&case_voisine.getCouleur()==couleurJeton){
        cpt += case_voisine.quatreHorizontal(this);
      }
    }
    return cpt;
  }

  /**
   * verifie si quatre pions sont alignés diagonalement
   * @param casedepart
   * @return le nombre de pions alignés 
   */
  public int quatreDiagonal(Case casedepart) {
    return 1;
  }
  



}
