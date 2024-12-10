package src;

import java.util.List;
import java.util.ArrayList;

class Client {
  private String IP;
  private String nomJoueur;
  private List<Plateau> listeMatchs;
  private int nbVictoires;
  private double pourcentVict;

  public  Client(String IP, String nomJoueur) {
    this.IP = IP;
    this.nomJoueur = nomJoueur;
    this.listeMatchs = new ArrayList<>();
    this.nbVictoires = 0;
    this.pourcentVict = 0;
  }

  public void updateVictoires() {
    this.nbVictoires += 1;
  }
  public void addMatchs(Plateau plateau){
    this.listeMatchs.add(plateau);
  }
  public void setPourcentVict(){
    this.pourcentVict = this.nbVictoires*100/listeMatchs.size();
  }
  public int getnbVictoires(){
    return this.nbVictoires;
  }
  public double getPourcentVict(){
    return this.pourcentVict;

  }
  public String historique(){
    return "";
  }
  public String getJoueur(){
    return this.nomJoueur;
  }

}
