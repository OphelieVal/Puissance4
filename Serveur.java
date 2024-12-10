
public class Serveur {

  private List<Plateau> lesPlateaux;
  private List<Joueur> lesJoueurs;
  private List<Client> lesClients;

  public Serveur() {
    this.lesPlateaux = new ArrayList<>();
    this.lesJoueurs = new ArrayList<>();
  }

  public String (String nomJoueur) {
    //TO DO connecte les joueur au serveur
    isPresent = false;
    for (Client client: this.lesClients) {
      isPresent = client.getNomJoueur().equals(nomJoueur);
    }
    if !(isPresent) {
        return "OK";
    }else {
        return "ERR";
    }
  }

  public String wait(String nomJoueur) {
  }

  public String ask(String nomJoueur) {
  }

  public String play(int nomColonne) {
  }

  public String update(String nomJoueur) {
  }

  public boolean win(String nomJoueur) {
  }

  public String end(String nomJoueur) {
  }

  public String getStats(String nomJoueur) {
  }

  public String quit(String nomJoueur) {

  }
}
