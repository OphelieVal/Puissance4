package modele;

import java.util.concurrent.locks.ReentrantLock;

public class Joueur {
  private boolean gameOver = false;
  private String username;
  private String couleur;
  private Plateau lePlateau;
  private ReentrantLock lock = new ReentrantLock();

  public Joueur(String username,String couleur){
    this.username = username;
    this.couleur = couleur;
  }

  public String getUsername() { return username; }
  public String getCouleur(){
    return this.couleur;
  }
  public Plateau getPlateau(){return this.lePlateau;}

  public void setUsername(String username) { this.username = username; }
  public void setCouleur(String couleur) { this.couleur = couleur; }
  public void setLePlateau(Plateau lePlateau) { this.lePlateau = lePlateau; }

  public boolean aGagne(){return false;}
  public void reset() {}
}
