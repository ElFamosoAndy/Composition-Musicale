package business;

public class Metadonne {
    // Nom de la partition (ex: "Sonate en Do majeur")
    private String nom;

    // Nom du compositeur (ex: "Mozart")
    private String compositeur;

    // Constructeur : initialise uniquement le nom (l’auteur peut être ajouté après)
    public Metadonne(String nom) {
        this.nom = nom;
    }

    // Getter pour le nom de la partition
    public String getNom() {
        return nom;
    }

    // Setter pour modifier le nom
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter pour le compositeur
    public String getCompositeur() {
        return compositeur;
    }

    // Setter pour modifier le compositeur
    public void setCompositeur(String compositeur) {
        this.compositeur = compositeur;
    }
}
