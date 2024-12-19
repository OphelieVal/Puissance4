package modele;

import java.util.ArrayList;
import java.util.List;

public class Case {
  private boolean aJeton = false;
  private String couleur = null;
  private List<Case> lesVoisines;

  public Case(){
    this.lesVoisines = new ArrayList<>();
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

  /** ose le jeton sur une case
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
}
