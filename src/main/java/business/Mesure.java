package business;

import java.util.ArrayList;
import java.util.List;

public class Mesure {
    // Signature rythmique de la mesure, ex: "4/4"
    private String signatureRythmique;

    // Liste des notes (ou silences) contenues dans la mesure
    private List<Note> notes;

    // Constructeur : initialise une mesure vide avec sa signature
    public Mesure(String signatureRythmique) {
        this.signatureRythmique = signatureRythmique;
        this.notes = new ArrayList<>();
    }

    // Getters / Setters
    public String getSignatureRythmique() {
        return signatureRythmique;
    }

    public void setSignatureRythmique(String signatureRythmique) {
        this.signatureRythmique = signatureRythmique;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    // Vérifie si on peut ajouter une note de cette durée dans la mesure
    public boolean peutAjouterNote(String duree) {
        int capaciteRestante = getCapaciteRestante();
        int valeurNote = getValeurDuree(duree);
        return valeurNote <= capaciteRestante;
    }

    // Ajoute une note à la mesure si c’est possible
    public void ajouterNote(Note note) {
        if (peutAjouterNote(note.getDuree())) {
            notes.add(note);
        } else {
            System.out.println("Impossible d'ajouter " + note.getDuree() + " : mesure pleine !");
        }
    }

    // Retourne la capacité restante de la mesure (en "valeurs de noires")
    private int getCapaciteRestante() {
        int capaciteTotale = 4; // Pour une mesure en 4/4 = 4 noires
        int somme = 0;
        for (Note note : notes) {
            somme += getValeurDuree(note.getDuree());
        }
        return capaciteTotale - somme;
    }

    // Convertit une durée (Noire, Blanche, Ronde) en "valeur"
    private int getValeurDuree(String duree) {
        return switch (duree) {
            case "Noire" -> 1;
            case "Blanche" -> 2;
            case "Ronde" -> 4;
            default -> 0;
        };
    }

    // Complète la mesure avec des silences noirs jusqu'à la remplir
    public void completerAvecSilences() {
        int capaciteRestante = getCapaciteRestante();
        while (capaciteRestante > 0) {
            if (capaciteRestante >= 1) {
                notes.add(new Note("Silence", "Noire", true));
                capaciteRestante -= 1;
            }
        }
    }
}
