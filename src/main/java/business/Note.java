package business;

public class Note {
    private String hauteur; // Do, Ré, Mi...
    private String duree;   // Ronde, Blanche, Noire, Croche...

    public Note(String hauteur, String duree) {
        this.hauteur = hauteur;
        this.duree = duree;
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
}
