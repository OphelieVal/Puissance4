package modele;

import java.awt.*;
import java.util.concurrent.locks.ReentrantLock;

public class Joueur {
  private boolean gameOver = false;
  private String nomJoueur;
  private String couleur;
  private Plateau lePlateau;
  private String currentColor;
  private ReentrantLock lock = new ReentrantLock();

  public Joueur(String nomJoueur,String couleur){
    this.nomJoueur = nomJoueur;
    this.couleur = couleur;
    this.lePlateau = null;

  }

  /** getter nom joueur
   * @return le nom du joueur
   */
  public String getNomJoueur() {return nomJoueur;}

  /** getter couleur joueur
   * @return couleur du joueur
   */
  public String getCouleur(){
    return this.couleur;
  }

  /** gette le plateau actuel
   * @return plateau actuel du joueur
   */
  public Plateau getPlateau(){return this.lePlateau;}

  /** setter nom joueur
   * @param nomJoueur nom du joueur
   */
  public void setNomJoueur(String nomJoueur) { 
    this.nomJoueur = nomJoueur; 
  }

  /** sette couleur jeu joueur
   * @param couleur couleur de jeu joueur
   */
  public void setCouleur(String couleur) { 
    this.couleur = couleur; 
  }

  /** setter le plateau de jeu actuel
   * @param lePlateau un plateau de jeu
   */
  public void setLePlateau(Plateau lePlateau) {
    this.lePlateau = lePlateau;
    this.currentColor =  this.lePlateau.getAColor();
  }

  public void setCurrentColor(String currentColor) {
    this.currentColor = currentColor;
  }

  /** verifie si le joueur a gagné apres chaque coup
   * @return true si le joueur a gagné sinon false
   */
  public boolean aGagne(int x, int y) {
    Case caseJouee = this.lePlateau.getCase(x, y);
    int count = 4;
    boolean winState = this.lePlateau.quatreHorizontal(caseJouee) >= count ||
            this.lePlateau.quatreVertical(caseJouee) >= count ||
            this.lePlateau.quatreDiagonal(caseJouee) >= count;
    return winState;
  }

  /** remet à jour l'ensemble des cases du plateau
   * 
   */
  public void reset() {
    this.gameOver = false;
    this.lePlateau.reset();
  }

  public boolean setInitialState() {
    this.gameOver = false;
    this.lePlateau = null;
    this.currentColor = null;
    return true;
  }
}
