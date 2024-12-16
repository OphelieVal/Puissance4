
class Joueur {
  private boolean gameOver = false;
  private String couleur;
  private Plateau lePlateau;

  public Joueur(String couleur, Plateau plateau){
    this.couleur = couleur;
    this.lePlateau = plateau;
  }

  public String getCouleur(){
    return this.couleur;
  }

  public boolean aGagne(){
    return false;
  }

  public void reset() {
  }

}
