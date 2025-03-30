package ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import controller.PartitionController;

public class PartitionView extends Canvas {
    private GraphicsContext gc;
    private PartitionController partitionController;
    
    private static final int LARGEUR_MESURE = 200; // Largeur d'une mesure
    private static final int HAUTEUR_LIGNE = 275; // Hauteur entre deux lignes
    private static final int MARGES_SUP_INF = 50; // Marges au-dessus et en dessous
    
    // AnimationTimer pour rafraîchir l'affichage en continu
    private AnimationTimer animationTimer;

    public PartitionView(PartitionController controller) {
        super(1000, 625); // Largeur fixe, mais hauteur ajustée dynamiquement
        this.partitionController = controller;
        this.gc = this.getGraphicsContext2D();
    }
    
    // Démarre l'animation (à appeler lors du début de la lecture)
    public void startAnimation() {
        if (animationTimer == null) {
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    mettreAJourAffichage();
                }
            };
            animationTimer.start();
        }
    }
    
    // Arrête l'animation (à appeler lorsque la lecture s'arrête)
    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
            // Pour être sûr de redessiner l'affichage final sans indication de lecture
            mettreAJourAffichage();
        }
    }

    public void mettreAJourAffichage() {
        int mesuresParLigne = 4; // Maximum 4 mesures par ligne
        int totalMesures = partitionController.getPartition().getMesures().size();
        int lignes = (int) Math.ceil((double) totalMesures / mesuresParLigne);

        // Calcul de la hauteur totale de la partition
        double hauteurTotale = lignes * HAUTEUR_LIGNE + MARGES_SUP_INF;

        // Ajuster la hauteur du Canvas dynamiquement
        setHeight(hauteurTotale);
        
        // Nettoyer le Canvas
        gc.clearRect(0, 0, getWidth(), getHeight());

        int mesureIndex = 0;
        for (int ligne = 0; ligne < lignes; ligne++) {
            int yOffset = ligne * HAUTEUR_LIGNE; // Décalage vertical pour chaque ligne
            
            int mesuresSurLigne = Math.min(mesuresParLigne, totalMesures - mesureIndex);
            dessinerPortee(yOffset, mesuresSurLigne);

            for (int i = 0; i < mesuresSurLigne; i++) {
                int mesureX = 100 + (i * LARGEUR_MESURE); // Position horizontale de la mesure
                
                // Passage de l'indice de la mesure pour le suivi de lecture
                NoteRenderer.dessinerNotes(gc, partitionController.getPartition().getMesures().get(mesureIndex).getNotes(), mesureX, yOffset, mesureIndex);

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

        int largeurPortee = 100 + (nombreMesures * LARGEUR_MESURE); // Largeur adaptée aux mesures présentes

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
            gc.strokeLine(mesureX + 200, 131 + yOffset, mesureX + 200, 209 + yOffset);
        } else {
            gc.strokeLine(mesureX + 200, 130 + yOffset, mesureX + 200, 210 + yOffset);
        }
    }
}
