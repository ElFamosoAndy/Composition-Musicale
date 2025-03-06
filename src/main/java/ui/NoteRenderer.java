package ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import business.Note;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteRenderer {
    private static final Map<String, Integer> notesPositions = new HashMap<>();
    private static final Map<String, Integer> durees = new HashMap<>();
    private static final Map<String, Integer> barresGraves = new HashMap<>();
    private static final Map<String, Integer> barresAigues = new HashMap<>();

    static {
        // Positions des notes
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

        // Durées des notes
        durees.put("Noire", 1);
        durees.put("Blanche", 2);
        durees.put("Ronde", 4);

        // Notes nécessitant une ligne supplémentaire en bas
        barresGraves.put("Si grave", 1);
        barresGraves.put("Do grave", 1);

        // Notes nécessitant des lignes supplémentaires en haut
        barresAigues.put("La aigu", 1);
        barresAigues.put("Si aigu", 1);
        barresAigues.put("Do aigu", 2);
        barresAigues.put("Ré aigu", 2);
        barresAigues.put("Mi aigu", 3);
        barresAigues.put("Fa aigu", 3);
    }

    public static void dessinerNotes(GraphicsContext gc, List<Note> notes) {
        int noteX = 100;

        for (Note note : notes) {
            int noteY = note.estSilence() ? 160 : notesPositions.get(note.getHauteur());
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BLACK);

            if (note.estSilence()) {
                // Dessiner le silence en fonction de la durée
                dessinerSilence(gc, noteX, note.getDuree());
            } else {
                // Dessiner la note en fonction de la durée
                dessinerNote(gc, noteX, noteY, durees.get(note.getDuree()));
            }

            // Dessiner les lignes supplémentaires pour les notes graves et aiguës
            dessinerLignesSupplementaires(gc, noteX, note.getHauteur());

            noteX += 50; // Espacement entre les notes
        }
    }

    private static void dessinerLignesSupplementaires(GraphicsContext gc, int noteX, String note) {
        // Lignes supplémentaires en bas (notes graves)
        if (barresGraves.containsKey(note)) {
            for (int i = 0; i < barresGraves.get(note); i++) {
                gc.strokeLine(noteX, 230 + (i * 20), noteX + 37, 230 + (i * 20));
            }
        }

        // Lignes supplémentaires en haut (notes aiguës)
        if (barresAigues.containsKey(note)) {
            for (int i = 0; i < barresAigues.get(note); i++) {
                gc.strokeLine(noteX, 110 - (i * 20), noteX + 37, 110 - (i * 20));
            }
        }
    }

    private static void dessinerNote(GraphicsContext gc, int x, int y, int duree) {
        gc.setLineWidth(2);
        switch (duree) {
            case 1: // Noire
                gc.setFont(new Font("Roboto", 100));
                gc.fillText("𝅘𝅥", x, y + 20);
                break;
            case 2: // Blanche
                gc.setFont(new Font("Roboto", 100));
                gc.fillText("𝅗𝅥", x, y + 20);
                break;
            case 4: // Ronde
                gc.setFont(new Font("Roboto", 90));
                gc.fillText("𝅝", x, y + 20);
                break;
        }
    }

    private static void dessinerSilence(GraphicsContext gc, int x, String duree) {
        System.out.println("silence");
        switch (duree) {
            case "Noire": //soupir
                gc.setFont(new Font("Roboto", 90));
                gc.fillText("𝄽", x, 195);
                break;
            case "Blanche": //demi-pause
                gc.fillRect(x, 160, 20, 10);
                break;
            case "Ronde": //pause
                gc.fillRect(x, 150, 20, 10);
                break;
        }
    }
}
