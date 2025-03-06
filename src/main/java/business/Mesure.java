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
}