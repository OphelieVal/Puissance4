package modele;

import java.util.concurrent.locks.ReentrantLock;

public class Joueur {
  private boolean gameOver = false;
  private String nomJoueur;
  private String couleur;
  private Plateau lePlateau;
  private ReentrantLock lock = new ReentrantLock();

  public Joueur(String nomJoueur,String couleur){
    this.nomJoueur = nomJoueur;
    this.couleur = couleur;
  }

  /** getter nom joueur
   * @return le nom du joueur
   */
  public String getNomJoueur() { 
    return nomJoueur; 
  }

  /** getter couleur joueur
   * @return couleur du joueur
   */
  public String getCouleur(){
    return this.couleur;
  }

  /** gette le plateau actuel
   * @return plateau actuel du joueur
   */
  public Plateau getPlateau(){
    return this.lePlateau;
  }

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
  }
 
  /** verifie si le joueur a gagné
   * @return true si le joueur a gagné sinon false
   */
  public boolean aGagne(){
    return false;
  }

  /** remet à jour l'ensemble des cases du plateau
   * 
   */
  public void reset() {}
}
