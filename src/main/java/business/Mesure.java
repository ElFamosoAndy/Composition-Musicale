package business;

import java.util.ArrayList;
import java.util.List;

public class Mesure {
    private String signatureRythmique;
    private List<Note> notes;

    public Mesure(String signatureRythmique/*, List<Note> notes*/) {
        this.signatureRythmique = signatureRythmique;
        this.notes = new ArrayList<>();
    }

    // Getters et setters
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

    public boolean peutAjouterNote(String duree) {
        int capaciteRestante = getCapaciteRestante();
        int valeurNote = getValeurDuree(duree);

        return valeurNote <= capaciteRestante;
    }

    public void ajouterNote(Note note) {
        if (peutAjouterNote(note.getDuree())) {
            notes.add(note);
        } else {
            System.out.println("❌ Impossible d'ajouter " + note.getDuree() + " : mesure pleine !");
        }
    }

    private int getCapaciteRestante() {
        int capaciteTotale = 4; // Une mesure en 4/4 = 4 noires
        int somme = 0;

        for (Note note : notes) {
            somme += getValeurDuree(note.getDuree());
        }

        return capaciteTotale - somme;
    }

    private int getValeurDuree(String duree) {
        return switch (duree) {
            case "Noire" -> 1;
            case "Blanche" -> 2;
            case "Ronde" -> 4;
            default -> 0;
        };
    }

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