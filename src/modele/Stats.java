package modele;

public class Stats {
    private Joueur joueur;
    private int NbVictoires=0;
    private int NbParties=0;
    private float PourcentVict=0;
    
    public Stats(Joueur joueur, int nbVictoires, int nbParties) {
        this.joueur = joueur;
        this.NbVictoires = nbVictoires;
        this.NbParties = nbParties;
        this.PourcentVict = nbVictoires/nbParties*100;
    }

    public Joueur getJoueur() {
        return this.joueur;
    }

    public int getNbVictoires() {
        return this.NbVictoires;
    }

    public void setNbVictoires(int nbVictoires) {
        this.NbVictoires = nbVictoires;
        this.PourcentVict = nbVictoires/this.NbParties*100;
    }

    public int getNbParties() {
        return this.NbParties;
    }

    public void setNbParties(int nbParties) {
        this.NbParties = nbParties;
        this.PourcentVict = this.NbVictoires/nbParties*100;
    }

    public float getPourcentVict() {
        return this.PourcentVict;
    }
}
