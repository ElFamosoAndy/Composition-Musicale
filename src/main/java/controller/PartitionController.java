package controller;

import business.*;
import data.GestionFichier;

public class PartitionController {
    private Partition partition;

    public PartitionController() {
        // Charger la partition sauvegardée ou créer une nouvelle
        Partition chargee = GestionFichier.chargerPartition();
        if (chargee != null) {
            this.partition = chargee;
        } else {
            this.partition = new Partition(new Metadonne("Nouvelle Partition"), 60, "Sol");
            partition.getMesures().add(new Mesure("4/4"));
        }
    }

    public Partition getPartition() {
        return partition;
    }

    public void ajouterNote(String hauteur, String duree) {
        Note note = new Note(hauteur, duree, false);
        partition.getMesures().get(0).getNotes().add(note);
    }

    public void ajouterSilence(String duree) {
        Note silence = new Note("Silence", duree, true);
        partition.getMesures().get(0).getNotes().add(silence);
    }

    public void sauvegarderPartition() {
        GestionFichier.sauvegarderPartition(partition);
    }

    public void chargerPartition() {
        Partition p = GestionFichier.chargerPartition();
        if (p != null) {
            this.partition = p;
        }
    }
}
