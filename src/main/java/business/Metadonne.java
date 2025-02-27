package business;

public class Metadonne {
    private String nom;
    private String compositeur;

    public Metadonne(String nom) {
        this.nom = nom;
    }

    // Getters et setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCompositeur() {
        return compositeur;
    }

    public void setCompositeur(String compositeur) {
        this.compositeur = compositeur;
    }
}
