package data;

import business.Partition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class GestionFichier {
    private static final String FICHIER_JSON = "partition.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Sauvegarde une partition en fichier JSON
     * @param partition Partition à sauvegarder
     */
    public static void sauvegarderPartition(Partition partition) {
        try (FileWriter writer = new FileWriter(FICHIER_JSON)) {
            gson.toJson(partition, writer);
            System.out.println("Partition sauvegardée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Charge une partition depuis un fichier JSON
     * @return Partition chargée, ou null si erreur
     */
    public static Partition chargerPartition() {
        try (FileReader reader = new FileReader(FICHIER_JSON)) {
            return gson.fromJson(reader, Partition.class);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }
}
