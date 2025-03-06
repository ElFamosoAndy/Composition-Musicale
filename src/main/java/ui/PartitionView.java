package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class PartitionView extends Canvas {
    private GraphicsContext gc;
    private int noteCount = 0; // Pour placer les notes à droite progressivement

    // Positions des notes (Do en bas, Mi en haut)
    private final Map<String, Integer> notesPositions = new HashMap<>();
    private final Map<String, Integer> durees = new HashMap<>();

    public PartitionView() {
        super(600, 300);
        gc = this.getGraphicsContext2D();
        initNotesPositions();
        initDurees();
        dessinerPortee();
    }

    private void initNotesPositions() {
        notesPositions.put("Si grave", 230);
        notesPositions.put("Do grave", 220);
        notesPositions.put("Ré grave", 210);
        notesPositions.put("Mi grave", 200);
        notesPositions.put("Fa grave", 190);
        notesPositions.put("Sol", 180);
        notesPositions.put("La", 170);
        notesPositions.put("Si", 160);
        notesPositions.put("Do", 150);
        notesPositions.put("Ré", 140);
        notesPositions.put("Mi", 130);
        notesPositions.put("Fa", 120);
        notesPositions.put("Sol aigu", 110);
        notesPositions.put("La aigu", 100);
        notesPositions.put("Si aigu", 90);
        notesPositions.put("Do aigu", 80);
        notesPositions.put("Ré aigu", 70);
        notesPositions.put("Mi aigu", 60);
        notesPositions.put("Fa aigu", 50);
    }

    private void initDurees() {
        durees.put("Noire", 1);
        durees.put("Blanche", 2);
        durees.put("Ronde", 4);
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
        /*gc.setFont(new Font("Arial", 68));
        gc.fillText("𝄢", 20, 180);*/
    }

    public void ajouterNote(String note, String duree) {
        if (!notesPositions.containsKey(note) || !durees.containsKey(duree)) return;
        
        int noteX = 100 + noteCount * 50;
        int noteY = notesPositions.get(note);
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        switch (durees.get(duree)) {
            case 1:
                gc.fillOval(noteX, noteY, 20, 20);
                gc.strokeLine(noteX + 19, noteY + 10, noteX + 19, noteY - 60);
                break;
            case 2:
                gc.strokeOval(noteX, noteY, 20, 20);
                gc.strokeLine(noteX + 19, noteY + 10, noteX + 19, noteY - 60);
                break;
            case 4:
                gc.strokeOval(noteX, noteY, 20, 20);
                break;
        
            default:
                break;
        }

        // Ajouter une ou des lignes supplémentaires si nécessaire
        Map<String, Integer> barresgraves = new HashMap<>();
        barresgraves.put("Si grave",1);
        barresgraves.put("Do grave",1);
        Map<String, Integer> barresaigues = new HashMap<>();
        barresaigues.put("La aigu",1);
        barresaigues.put("Si aigu",1);
        barresaigues.put("Do aigu",2);
        barresaigues.put("Ré aigu",2);
        barresaigues.put("Mi aigu",3);
        barresaigues.put("Fa aigu",3);
        if (barresgraves.containsKey(note)) {
            for (int i = 0; i < barresgraves.get(note); i++) {
                gc.strokeLine(noteX - 10, 230 + (i * 20), noteX + 30, 230 + (i * 20));
            }
        }else if (barresaigues.containsKey(note)) {
            for (int i = 0; i < barresaigues.get(note); i++) {
                gc.strokeLine(noteX - 10, 110 - (i * 20), noteX + 30, 110 - (i * 20));
            }
        }

        noteCount++;
    }
}
