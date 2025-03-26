package controller;

import audio.LecteurMIDI;
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
        Mesure mesureActuelle = partition.getMesures().get(partition.getMesures().size() - 1);
        
        if (!mesureActuelle.peutAjouterNote(duree)) {
            System.out.println("⚠️ Mesure pleine ! Création d'une nouvelle mesure.");
            mesureActuelle.completerAvecSilences();
            mesureActuelle = new Mesure("4/4");
            partition.getMesures().add(mesureActuelle);
        }
    
        Note note = new Note(hauteur, duree, false);
        mesureActuelle.ajouterNote(note);
    }

    public void ajouterSilence(String duree) {
        Mesure mesureActuelle = partition.getMesures().get(partition.getMesures().size() - 1);
        
        if (!mesureActuelle.peutAjouterNote(duree)) {
            System.out.println("⚠️ Mesure pleine ! Création d'une nouvelle mesure.");
            mesureActuelle.completerAvecSilences();
            mesureActuelle = new Mesure("4/4");
            partition.getMesures().add(mesureActuelle);
        }
    
        Note silence = new Note("Silence", duree, true);
        mesureActuelle.ajouterNote(silence);
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

    public void lirePartition() {
        LecteurMIDI.jouerPartition(partition);
    }
}
