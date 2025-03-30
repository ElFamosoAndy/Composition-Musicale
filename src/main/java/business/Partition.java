package business;

import java.util.ArrayList;
import java.util.List;

public class Partition {
    // Métadonnées : nom de la partition, compositeur, etc.
    private Metadonne metadonnes;

    // Tempo de la partition (en BPM, ex : 60)
    private int tempo;

    // Armure (ex: "Sol" pour une armure avec un dièse) — TODO : peut être déplacée dans Mesure
    private String armure;

    // Liste des mesures qui composent la partition
    private List<Mesure> mesures;

    // Constructeur : initialise la partition avec ses données de base
    public Partition(Metadonne metadonnes, int tempo, String armure) {
        this.metadonnes = metadonnes;
        this.tempo = tempo;
        this.armure = armure;
        this.mesures = new ArrayList<>(); // Liste vide au départ
    }

    // Getter/Setter des métadonnées
    public Metadonne getMetadonnes() {
        return metadonnes;
    }

    public void setMetadonnes(Metadonne metadonnes) {
        this.metadonnes = metadonnes;
    }

    // Getter/Setter du tempo
    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    // Getter/Setter de l’armure
    public String getArmure() {
        return armure;
    }

    public void setArmure(String armure) {
        this.armure = armure;
    }

    // Getter/Setter des mesures
    public List<Mesure> getMesures() {
        return mesures;
    }

    public void setMesures(List<Mesure> mesures) {
        this.mesures = mesures;
    }
}
