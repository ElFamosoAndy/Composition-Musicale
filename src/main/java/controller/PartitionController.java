package controller;

import audio.LecteurMIDI;
import business.*;
import data.GestionFichier;
import java.io.File;
import java.util.logging.Logger;

public class PartitionController {
    private Partition partition;
    private String currentFilePath; // Chemin du fichier courant (null si jamais sauvegardé)
    private static final Logger logger = Logger.getLogger(PartitionController.class.getName());

    public PartitionController() {
        Partition chargee = GestionFichier.chargerPartition();
        if (chargee != null) {
            this.partition = chargee;
            String sanitizedName = GestionFichier.sanitizeFilename(partition.getMetadonnes().getNom());
            if (sanitizedName.isEmpty()) {
                sanitizedName = "NouvellePartition";
            }
            this.currentFilePath = "partitions/" + sanitizedName + ".json";
            logger.info("Partition chargée depuis le fichier par défaut: " + currentFilePath);
        } else {
            this.partition = new Partition(new Metadonne("Nouvelle Partition"), 60, "Sol");
            partition.getMesures().add(new Mesure("4/4"));
            this.currentFilePath = null;
            logger.info("Nouvelle partition créée.");
        }
    }

    public Partition getPartition() {
        return partition;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String path) {
        this.currentFilePath = path;
    }

    public void ajouterNote(String hauteur, String duree) {
        Mesure mesureActuelle = partition.getMesures().get(partition.getMesures().size() - 1);
        if (!mesureActuelle.peutAjouterNote(duree)) {
            logger.warning("⚠️ Mesure pleine ! Création d'une nouvelle mesure.");
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
            logger.warning("⚠️ Mesure pleine ! Création d'une nouvelle mesure.");
            mesureActuelle.completerAvecSilences();
            mesureActuelle = new Mesure("4/4");
            partition.getMesures().add(mesureActuelle);
        }
        Note silence = new Note("Silence", duree, true);
        mesureActuelle.ajouterNote(silence);
    }

    public void sauvegarderPartition() {
        if (currentFilePath == null || !(new File(currentFilePath).exists())) {
            logger.info("Aucun fichier existant. Veuillez utiliser 'Enregistrer sous'.");
        } else {
            GestionFichier.sauvegarderPartition(partition, currentFilePath);
            logger.info("Partition sauvegardée dans " + currentFilePath);
        }
    }

    public void sauvegarderPartition(String filePath) {
        this.currentFilePath = filePath;
        GestionFichier.sauvegarderPartition(partition, filePath);
        logger.info("Partition sauvegardée dans " + filePath);
    }

    public void chargerPartition(String filePath) {
        Partition p = GestionFichier.chargerPartition(filePath);
        if (p != null) {
            this.partition = p;
            this.currentFilePath = filePath;
            logger.info("Partition chargée depuis " + filePath);
        } else {
            logger.warning("Echec du chargement de la partition depuis " + filePath);
        }
    }

    public void lirePartition() {
        LecteurMIDI.jouerPartition(partition);
    }
    
    // Méthode pour revenir en arrière en annulant la dernière action
    public void revenirEnArriere() {
        if (partition.getMesures().isEmpty()) {
            logger.info("Aucune action à annuler.");
            return;
        }
        Mesure lastMeasure = partition.getMesures().get(partition.getMesures().size() - 1);
        if (!lastMeasure.getNotes().isEmpty()) {
            lastMeasure.getNotes().remove(lastMeasure.getNotes().size() - 1);
            logger.info("Dernière action annulée (note/silence supprimé).");
            // Si la mesure devient vide et qu'il y a plusieurs mesures, on peut la supprimer
            if (lastMeasure.getNotes().isEmpty() && partition.getMesures().size() > 1) {
                partition.getMesures().remove(partition.getMesures().size() - 1);
                logger.info("Mesure vide supprimée.");
            }
        } else if (partition.getMesures().size() > 1) {
            partition.getMesures().remove(partition.getMesures().size() - 1);
            logger.info("Dernière mesure supprimée.");
        } else {
            logger.info("Aucune action à annuler.");
        }
    }
}
