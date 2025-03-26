package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import controller.PartitionController;

public class PartitionView extends Canvas {
    private GraphicsContext gc;
    private PartitionController partitionController;

    public PartitionView(PartitionController controller) {
        super(1000, 600);
        this.partitionController = controller;
        this.gc = this.getGraphicsContext2D();
        //dessinerPortee();
    }

    public void mettreAJourAffichage() {
        gc.clearRect(0, 0, getWidth(), getHeight());
    
        int mesuresParLigne = 4;  // Maximum 4 mesures par ligne
        int largeurMesure = 200;  // Largeur d'une mesure
        int hauteurLigne = 200;   // Espacement vertical entre les lignes
    
        int totalMesures = partitionController.getPartition().getMesures().size();
        int lignes = (int) Math.ceil((double) totalMesures / mesuresParLigne);
    
        int mesureIndex = 0;
        for (int ligne = 0; ligne < lignes; ligne++) {
            int yOffset = ligne * hauteurLigne; // Décalage vertical pour chaque ligne
    
            // Nombre réel de mesures sur cette ligne (utile pour la dernière ligne)
            int mesuresSurLigne = Math.min(mesuresParLigne, totalMesures - mesureIndex);
            dessinerPortee(yOffset, mesuresSurLigne);
    
            for (int i = 0; i < mesuresSurLigne; i++) {
                int mesureX = 100 + (i * largeurMesure);  // Position horizontale de la mesure
                NoteRenderer.dessinerNotes(gc, partitionController.getPartition().getMesures().get(mesureIndex).getNotes(), mesureX, yOffset);
    
                boolean estDerniereMesure = (mesureIndex == totalMesures - 1);
                dessinerBarresDeMesure(mesureX, yOffset, estDerniereMesure);
    
                mesureIndex++;
            }
        }
    }    

    private void dessinerPortee(int yOffset, int nombreMesures) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        int startY = 130 + yOffset;
        int lineSpacing = 20;
        
        int largeurPortee = 100 + (nombreMesures * 200);  // La portée s'arrête après la dernière mesure
    
        for (int i = 0; i < 5; i++) {
            gc.strokeLine(10, startY + (i * lineSpacing), largeurPortee, startY + (i * lineSpacing));
        }
    
        // Dessiner la clé de sol et la première barre verticale
        gc.strokeLine(10, 130 + yOffset, 10, 210 + yOffset);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Roboto", 120));
        gc.fillText("𝄞", 20, 210 + yOffset);
    }    

    private void dessinerBarresDeMesure(int mesureX, int yOffset, boolean estDerniereMesure) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        if (estDerniereMesure) {
            gc.strokeLine(mesureX + 195, 130 + yOffset, mesureX + 195, 210 + yOffset);
            gc.setLineWidth(4);
            gc.strokeLine(mesureX + 200, 131 + yOffset, mesureX + 200, 209 + yOffset);  // Barre verticale entre chaque mesure
        } else {
            gc.strokeLine(mesureX + 200, 130 + yOffset, mesureX + 200, 210 + yOffset);  // Barre verticale entre chaque mesure
        }
    }
}
