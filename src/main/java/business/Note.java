package business;

public class Note {
    // Nom de la hauteur de la note (Do, Ré, Mi, etc.)
    private String hauteur;

    // Durée de la note (Croche, Noire, Blanche, Ronde...)
    private String duree;

    // Indique si la note est un silence (true) ou une note jouée (false)
    private boolean estSilence;

    // Constructeur : crée une note avec sa hauteur, durée et si c'est un silence
    public Note(String hauteur, String duree, boolean estSilence) {
        this.hauteur = hauteur;
        this.duree = duree;
        this.estSilence = estSilence;
    }

    // Getter pour la hauteur (ex : "Do", "Ré", etc.)
    public String getHauteur() {
        return hauteur;
    }

    // Setter pour changer la hauteur
    public void setHauteur(String hauteur) {
        this.hauteur = hauteur;
    }

    // Getter pour la durée (ex : "Noire", "Ronde", etc.)
    public String getDuree() {
        return duree;
    }

    // Setter pour changer la durée
    public void setDuree(String duree) {
        this.duree = duree;
    }

    // Indique si cette note est un silence
    public boolean estSilence() {
        return estSilence;
    }
}
