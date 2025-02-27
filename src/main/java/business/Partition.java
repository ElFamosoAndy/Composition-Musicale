package business;

import java.util.List;

public class Partition {
    private List<Metadonne> metadonnes;
    private int tempo;
    private String armure; //A déplacer dans Mesure.java
    private List<Mesure> mesures;

    public Partition(List<Metadonne> metadonnes, int tempo, String armure, List<Mesure> mesures) {
        this.metadonnes = metadonnes;
        this.tempo = tempo;
        this.armure = armure;
        this.mesures = mesures;
    }

    // Getters et setters
    public List<Metadonne> getMetadonnes() {
        return metadonnes;
    }

    public void setMetadonnes(List<Metadonne> metadonnes) {
        this.metadonnes = metadonnes;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public String getArmure() {
        return armure;
    }

    public void setArmure(String armure) {
        this.armure = armure;
    }

    public List<Mesure> getMesures() {
        return mesures;
    }

    public void setMesures(List<Mesure> mesures) {
        this.mesures = mesures;
    }
}