package ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import controller.PartitionController;

public class PartitionView extends Canvas {
    private GraphicsContext gc; // Outil de dessin
    private PartitionController partitionController; // Accès à la partition en cours

    // Constantes d’affichage
    private static final int LARGEUR_MESURE = 200;
    private static final int HAUTEUR_LIGNE = 275;
    private static final int MARGES_SUP_INF = 50;

    // Pour dessiner la lecture en direct (actualise l'affichage à chaque frame)
    private AnimationTimer animationTimer;

    // Constructeur : initialise la zone de dessin avec une taille fixe
    public PartitionView(PartitionController controller) {
        super(910, 625);
        this.partitionController = controller;
        this.gc = this.getGraphicsContext2D();
    }

    // Démarre l’animation pendant la lecture de la partition
    public void startAnimation() {
        if (animationTimer == null) {
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    mettreAJourAffichage(); // Redessine à chaque frame
                }
            };
            animationTimer.start();
        }
    }

    // Arrête l’animation et redessine proprement l'état final
    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
            mettreAJourAffichage(); // Affichage final
        }
    }

    // Méthode principale pour dessiner toute la partition
    public void mettreAJourAffichage() {
        int mesuresParLigne = 4;
        int totalMesures = partitionController.getPartition().getMesures().size();
        int lignes = (int) Math.ceil((double) totalMesures / mesuresParLigne);

        // Calcul dynamique de la hauteur du canevas
        double hauteurTotale = lignes * HAUTEUR_LIGNE + MARGES_SUP_INF;
        setHeight(hauteurTotale);

        // Efface tout le canvas
        gc.clearRect(0, 0, getWidth(), getHeight());

        int mesureIndex = 0;

        // Parcourt chaque ligne (groupes de 4 mesures)
        for (int ligne = 0; ligne < lignes; ligne++) {
            int yOffset = ligne * HAUTEUR_LIGNE;
            int mesuresSurLigne = Math.min(mesuresParLigne, totalMesures - mesureIndex);

            // Dessine la portée (5 lignes horizontales + clé de sol)
            dessinerPortee(yOffset, mesuresSurLigne);

            // Dessine chaque mesure sur cette ligne
            for (int i = 0; i < mesuresSurLigne; i++) {
                int mesureX = 100 + (i * LARGEUR_MESURE); // Position horizontale

                // Affiche les notes de la mesure via NoteRenderer
                NoteRenderer.dessinerNotes(
                    gc,
                    partitionController.getPartition().getMesures().get(mesureIndex).getNotes(),
                    mesureX,
                    yOffset,
                    mesureIndex
                );

                // Dessine la barre verticale à droite de la mesure
                boolean estDerniereMesure = (mesureIndex == totalMesures - 1);
                dessinerBarresDeMesure(mesureX, yOffset, estDerniereMesure);

                mesureIndex++;
            }
        }
    }

    // Dessine les 5 lignes d’une portée + clé de sol et première barre
    private void dessinerPortee(int yOffset, int nombreMesures) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        int startY = 130 + yOffset;
        int lineSpacing = 20;
        int largeurPortee = 100 + (nombreMesures * LARGEUR_MESURE);

        // 5 lignes parallèles horizontales
        for (int i = 0; i < 5; i++) {
            gc.strokeLine(10, startY + (i * lineSpacing), largeurPortee, startY + (i * lineSpacing));
        }

        // Première barre verticale et clé de sol
        gc.strokeLine(10, 130 + yOffset, 10, 210 + yOffset);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Roboto", 120));
        gc.fillText("𝄞", 20, 210 + yOffset);
    }

    // Dessine la barre de fin de mesure (double pour la dernière)
    private void dessinerBarresDeMesure(int mesureX, int yOffset, boolean estDerniereMesure) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        if (estDerniereMesure) {
            // Barre double pour la dernière mesure
            gc.strokeLine(mesureX + 195, 130 + yOffset, mesureX + 195, 210 + yOffset);
            gc.setLineWidth(4);
            gc.strokeLine(mesureX + 200, 131 + yOffset, mesureX + 200, 209 + yOffset);
        } else {
            // Barre simple
            gc.strokeLine(mesureX + 200, 130 + yOffset, mesureX + 200, 210 + yOffset);
        }
    }
}
