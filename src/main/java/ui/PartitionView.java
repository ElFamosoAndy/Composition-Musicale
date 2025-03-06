package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import business.Note;
import controller.PartitionController;
import java.util.List;

public class PartitionView extends Canvas {
    private GraphicsContext gc;
    private PartitionController partitionController;

    public PartitionView(PartitionController controller) {
        super(600, 300);
        this.partitionController = controller;
        this.gc = this.getGraphicsContext2D();
        dessinerPortee();
    }

    private void dessinerPortee() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        int startY = 130;
        int lineSpacing = 20;
        for (int i = 0; i < 5; i++) {
            gc.strokeLine(10, startY + (i * lineSpacing), 550, startY + (i * lineSpacing));
        }
        gc.strokeLine(10, 130, 10, 210);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Roboto", 120));
        gc.fillText("𝄞", 20, 210);
    }

    public void mettreAJourAffichage() {
        gc.clearRect(0, 0, getWidth(), getHeight()); // Effacer l'affichage
        dessinerPortee(); // Redessiner la portée

        List<Note> notes = partitionController.getPartition().getMesures().get(0).getNotes();
        NoteRenderer.dessinerNotes(gc, notes);
    }
}
