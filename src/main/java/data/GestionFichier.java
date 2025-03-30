package data;

import business.Partition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class GestionFichier {
    // Répertoire par défaut où les partitions sont enregistrées
    private static final String DEFAULT_DIRECTORY = "partitions";

    // Objet Gson pour convertir Java ↔ JSON avec un affichage bien indenté
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Nettoie le nom du fichier : supprime les caractères interdits pour les noms de fichier
     */
    public static String sanitizeFilename(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "");
    }

    /**
     * Sauvegarde une partition dans le dossier par défaut (en utilisant son nom comme nom de fichier)
     */
    public static void sauvegarderPartition(Partition partition) {
        // Crée le dossier "partitions" s'il n'existe pas
        File dir = new File(DEFAULT_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Nettoie le nom du fichier
        String sanitizedName = sanitizeFilename(partition.getMetadonnes().getNom());
        if (sanitizedName.isEmpty()) {
            sanitizedName = "NouvellePartition";
        }

        String filePath = DEFAULT_DIRECTORY + "/" + sanitizedName + ".json";

        // Écrit le fichier JSON
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(partition, writer);
            System.out.println("Partition sauvegardée avec succès dans " + filePath + " !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Charge une partition depuis un fichier JSON du répertoire par défaut
     * (par défaut : "partitions/NouvellePartition.json")
     */
    public static Partition chargerPartition() {
        String filePath = DEFAULT_DIRECTORY + "/NouvellePartition.json";
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Partition.class);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }

    /**
     * Sauvegarde une partition dans un fichier spécifique (utilisé pour "Enregistrer sous")
     */
    public static void sauvegarderPartition(Partition partition, String filePath) {
        // Vérifie si le dossier parent existe, sinon le crée
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Écrit le fichier JSON
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(partition, writer);
            System.out.println("Partition sauvegardée avec succès dans " + filePath + " !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde dans " + filePath + " : " + e.getMessage());
        }
    }

    /**
     * Charge une partition à partir d’un fichier JSON donné
     */
    public static Partition chargerPartition(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Partition.class);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement depuis " + filePath + " : " + e.getMessage());
            return null;
        }
    }
}
