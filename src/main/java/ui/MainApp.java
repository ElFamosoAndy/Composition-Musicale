package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.sound.midi.*;

import business.*;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Partition partition = new Partition(new Metadonne("Partition"),60,"Sol");
        partition.getMesures().add(new Mesure("4/4"));

        primaryStage.setTitle(partition.getMetadonnes().getNom());

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Fichier");
        MenuItem newPartition = new MenuItem("Nouvelle partition");
        MenuItem savePartition = new MenuItem("Enregistrer");
        MenuItem exportPartition = new MenuItem("Exporter au format PDF");
        menuFile.getItems().addAll(newPartition, savePartition, exportPartition);
        menuBar.getMenus().add(menuFile);

        PartitionView partitionView = new PartitionView();

        HBox controls = new HBox(10);
        ComboBox<String> noteSelector = new ComboBox<>();
        noteSelector.getItems().addAll("Si grave", "Do grave", "Ré grave", "Mi grave", "Fa grave", "Sol", "La", "Si", "Do", "Ré", "Mi", "Fa", "Sol aigu", "La aigu", "Si aigu", "Do aigu", "Ré aigu", "Mi aigu", "Fa aigu");
        noteSelector.setValue("Do");
        Button addNote = new Button("Ajouter Note");
        ComboBox<String> dureeSelector = new ComboBox<>();
        dureeSelector.getItems().addAll("Noire", "Blanche", "Ronde");
        dureeSelector.setValue("Noire");
        addNote.setOnAction(e -> {
            String selectedNote = noteSelector.getValue();
            String selectedDuree = dureeSelector.getValue();
            partitionView.ajouterNote(selectedNote, selectedDuree);
            partition.getMesures().get(0).getNotes().add(new Note(selectedNote, selectedDuree));
            jouerSon(selectedNote, selectedDuree);
        });
        Button readParition = new Button("Lire la partition");
        readParition.setOnAction(e -> {
            jouerPartition(partition);
        });

        controls.getChildren().addAll(noteSelector, dureeSelector, addNote, readParition);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(partitionView);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
   
    private int hauteurToInt(String hauteur) {
        int noteMidi = switch (hauteur) {
            case "Si grave" -> 47;
            case "Do grave" -> 48;
            case "Ré grave" -> 50;
            case "Mi grave" -> 52;
            case "Fa grave" -> 53;
            case "Sol" -> 55;
            case "La" -> 57;
            case "Si" -> 59;
            case "Do" -> 60;
            case "Ré" -> 62;
            case "Mi" -> 64;
            case "Fa" -> 65;
            case "Sol aigu" -> 67;
            case "La aigu" -> 69;
            case "Si aigu" -> 71;
            case "Do aigu" -> 72;
            case "Ré aigu" -> 74;
            case "Mi aigu" -> 76;
            case "Fa aigu" -> 77;
            default -> -1;
        };
        return noteMidi;
    }

    private int dureeToInt(String duree) {
        int dureeMidi = switch (duree) {
            case "Noire" -> 1000;
            case "Blanche" -> 2000;
            case "Ronde" -> 4000;
            default -> -1;
        };
        return dureeMidi;
    }

    private void jouerSon(String note, String duree) {
        int noteMidi = hauteurToInt(note);

        int dureeMidi = dureeToInt(duree);

        if (noteMidi == -1 || dureeMidi == -1) return;

        new Thread(() -> {
            try {
                Synthesizer synth = MidiSystem.getSynthesizer();
                synth.open();
                MidiChannel[] channels = synth.getChannels();
                channels[0].noteOn(noteMidi, 80);
                Thread.sleep(dureeMidi);
                channels[0].noteOff(noteMidi);
                synth.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void jouerPartition(Partition partition) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();
    
            for (Note note : partition.getMesures().get(0).getNotes()) {
                int hauteur = hauteurToInt(note.getHauteur()); // Pitch MIDI (0-127)
                int duree = dureeToInt(note.getDuree()); // Durée en ms
    
                channels[0].noteOn(hauteur, 80); // Joue la note avec une vélocité de 80
                Thread.sleep(duree); // Attendre la durée de la note
                channels[0].noteOff(hauteur); // Arrête la note
            }
    
            synth.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
