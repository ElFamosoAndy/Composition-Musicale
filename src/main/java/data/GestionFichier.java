package data;

import business.Partition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class GestionFichier {
    private static final String DEFAULT_DIRECTORY = "partitions";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Méthode utilitaire pour nettoyer le nom du fichier.
     * Supprime les caractères interdits: \ / : * ? " < > |
     */
    public static String sanitizeFilename(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "");
    }

    /**
     * Sauvegarde une partition en fichier JSON dans le répertoire par défaut,
     * en utilisant le nom de la partition (après nettoyage) suivi de ".json".
     * @param partition Partition à sauvegarder
     */
    public static void sauvegarderPartition(Partition partition) {
        // S'assurer que le répertoire par défaut existe
        File dir = new File(DEFAULT_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String sanitizedName = sanitizeFilename(partition.getMetadonnes().getNom());
        if (sanitizedName.isEmpty()) {
            sanitizedName = "NouvellePartition";
        }
        String filePath = DEFAULT_DIRECTORY + "/" + sanitizedName + ".json";
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(partition, writer);
            System.out.println("Partition sauvegardée avec succès dans " + filePath + " !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Charge une partition depuis un fichier JSON situé dans le répertoire par défaut.
     * Ici, on suppose par défaut que le fichier s'appelle "NouvellePartition.json".
     * @return Partition chargée, ou null en cas d'erreur
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
     * Sauvegarde une partition en fichier JSON dans le chemin spécifié.
     * @param partition Partition à sauvegarder
     * @param filePath Chemin complet du fichier
     */
    public static void sauvegarderPartition(Partition partition, String filePath) {
        // S'assurer que le répertoire du fichier existe
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(partition, writer);
            System.out.println("Partition sauvegardée avec succès dans " + filePath + " !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde dans " + filePath + " : " + e.getMessage());
        }
    }

    /**
     * Charge une partition depuis un fichier JSON dans le chemin spécifié.
     * @param filePath Chemin complet du fichier
     * @return Partition chargée, ou null en cas d'erreur
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
