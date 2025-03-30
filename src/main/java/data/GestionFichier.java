package data;

// Importations JavaFX pour l'interface graphique
import business.Partition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

// Importations pour générer le PDF avec PDFBox
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

// Importations standards Java et AWT
import java.io.*;
import java.awt.image.BufferedImage;

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

    // Méthode pour exporter le conteneur en PDF au format A4
    public static void exporterEnPDF(VBox partitionContainer, File file) {
        // Capturer un snapshot du conteneur
        WritableImage snapshot = partitionContainer.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
        
        try (PDDocument document = new PDDocument()) {
            // Utiliser le format A4 pour la page PDF
            PDRectangle a4 = PDRectangle.A4;
            PDPage page = new PDPage(a4);
            document.addPage(page);
            
            // Calculer le facteur de redimensionnement pour que l'image rentre dans la page A4
            float scaleX = a4.getWidth() / bufferedImage.getWidth();
            float scaleY = a4.getHeight() / bufferedImage.getHeight();
            float scale = Math.min(scaleX, scaleY);
            float imageWidth = bufferedImage.getWidth() * scale;
            float imageHeight = bufferedImage.getHeight() * scale;
            // Positionner l'image en haut au centre : centrer horizontalement et placer l'image en haut avec une marge de 20
            float posX = (a4.getWidth() - imageWidth) / 2;
            float marginTop = 20;
            float posY = a4.getHeight() - imageHeight - marginTop;
            
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(pdImage, posX, posY, imageWidth, imageHeight);
            }
            
            document.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
