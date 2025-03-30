package controller;

import audio.LecteurMIDI;
import business.*;
import data.GestionFichier;
import java.io.File;
import java.util.logging.Logger;

public class PartitionController {
    private Partition partition; // L'objet principal contenant la musique (métadonnées, mesures, etc.)
    private String currentFilePath; // Chemin du fichier où la partition est sauvegardée (null si nouveau)
    private static final Logger logger = Logger.getLogger(PartitionController.class.getName());

    // Constructeur : crée une nouvelle partition vide par défaut
    public PartitionController() {
        this.partition = new Partition(new Metadonne("Nouvelle Partition"), 60, "Sol");
        partition.getMesures().add(new Mesure("4/4")); // Ajoute une première mesure vide
        this.currentFilePath = null;
        logger.info("Nouvelle partition créée.");
    }

    // Getter pour accéder à la partition actuelle
    public Partition getPartition() {
        return partition;
    }

    // Getter et setter pour le chemin du fichier (utile pour savoir si on enregistre ou on fait "enregistrer sous")
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String path) {
        this.currentFilePath = path;
    }

    // Ajoute une note à la dernière mesure (crée une nouvelle mesure si besoin)
    public void ajouterNote(String hauteur, String duree) {
        Mesure mesureActuelle = partition.getMesures().get(partition.getMesures().size() - 1);
        if (!mesureActuelle.peutAjouterNote(duree)) {
            logger.warning("Mesure pleine ! Création d'une nouvelle mesure.");
            mesureActuelle.completerAvecSilences(); // Remplit la mesure avec des silences restants
            mesureActuelle = new Mesure("4/4");
            partition.getMesures().add(mesureActuelle);
        }
        Note note = new Note(hauteur, duree, false); // Création de la note
        mesureActuelle.ajouterNote(note);
    }

    // Ajoute un silence à la dernière mesure
    public void ajouterSilence(String duree) {
        Mesure mesureActuelle = partition.getMesures().get(partition.getMesures().size() - 1);
        if (!mesureActuelle.peutAjouterNote(duree)) {
            logger.warning("Mesure pleine ! Création d'une nouvelle mesure.");
            mesureActuelle.completerAvecSilences();
            mesureActuelle = new Mesure("4/4");
            partition.getMesures().add(mesureActuelle);
        }
        Note silence = new Note("Silence", duree, true);
        mesureActuelle.ajouterNote(silence);
    }

    // Sauvegarde la partition (en utilisant le chemin existant si présent)
    public void sauvegarderPartition() {
        if (currentFilePath == null || !(new File(currentFilePath).exists())) {
            logger.info("Aucun fichier existant. Veuillez utiliser 'Enregistrer sous'.");
        } else {
            GestionFichier.sauvegarderPartition(partition, currentFilePath);
            logger.info("Partition sauvegardée dans " + currentFilePath);
        }
    }

    // Sauvegarde dans un chemin spécifique (comme "enregistrer sous")
    public void sauvegarderPartition(String filePath) {
        this.currentFilePath = filePath;
        GestionFichier.sauvegarderPartition(partition, filePath);
        logger.info("Partition sauvegardée dans " + filePath);
    }

    // Charge une partition depuis un fichier JSON
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

    // Joue la partition via LecteurMIDI
    public void lirePartition() {
        LecteurMIDI.jouerPartition(partition);
    }

    // Permet d'annuler la dernière action (note ou silence ajouté)
    public void revenirEnArriere() {
        if (partition.getMesures().isEmpty()) {
            logger.info("Aucune action à annuler.");
            return;
        }

        Mesure lastMeasure = partition.getMesures().get(partition.getMesures().size() - 1);

        if (!lastMeasure.getNotes().isEmpty()) {
            // Supprime la dernière note/silence
            lastMeasure.getNotes().remove(lastMeasure.getNotes().size() - 1);
            logger.info("Dernière action annulée (note/silence supprimé).");

            // Si la mesure est maintenant vide et qu’il y a plusieurs mesures, on la supprime
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
