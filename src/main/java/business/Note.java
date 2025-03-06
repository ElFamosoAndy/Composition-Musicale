package business;

public class Note {
    private String hauteur; // Do, Ré, Mi...
    private String duree;   // Ronde, Blanche, Noire, Croche...
    private boolean estSilence;

    public Note(String hauteur, String duree, boolean estSilence) {
        this.hauteur = hauteur;
        this.duree = duree;
        this.estSilence = estSilence;
    }

    // Getters et setters
    public String getHauteur() {
        return hauteur;
    }

    public void setHauteur(String hauteur) {
        this.hauteur = hauteur;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public boolean estSilence() {
        return estSilence;
    }
}
