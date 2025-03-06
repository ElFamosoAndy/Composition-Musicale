package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.sound.midi.*;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Composition Musicale");

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Fichier");
        MenuItem newPartition = new MenuItem("Nouvelle Partition");
        menuFile.getItems().addAll(newPartition);
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
            jouerSon(selectedNote);
        });

        controls.getChildren().addAll(noteSelector, dureeSelector, addNote);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(partitionView);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void jouerSon(String note) {
        int noteMidi = switch (note) {
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

        if (noteMidi == -1) return;

        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();
            channels[0].noteOn(noteMidi, 80);
            Thread.sleep(500);
            channels[0].noteOff(noteMidi);
            synth.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
