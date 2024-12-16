package Modele;
import java.util.ArrayList;
import java.util.List;

public class Case {
  private boolean aJeton = false;
  private String couleur = null;
  private List<Case> lesVoisines;

  public Case(){
    this.lesVoisines = new ArrayList<>();
  }

  public void reset(){
    this.aJeton = false;
    this.couleur = null;
  }

  public boolean contientJeton(){
    return this.aJeton;
  }

  public boolean poseJeton(String couleur){
    this.aJeton = true;
    this.couleur = couleur;
    return true;
  }
}
