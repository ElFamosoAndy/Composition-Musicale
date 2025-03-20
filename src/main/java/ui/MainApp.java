package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import audio.LecteurMIDI;
import business.Note;
import controller.PartitionController;

public class MainApp extends Application {
    private PartitionController partitionController;
    private PartitionView partitionView;

    @Override
    public void start(Stage primaryStage) {

        partitionController = new PartitionController();
        partitionView = new PartitionView(partitionController);

        partitionView.mettreAJourAffichage();

        primaryStage.setTitle(partitionController.getPartition().getMetadonnes().getNom());

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Fichier");

        MenuItem savePartition = new MenuItem("Enregistrer");
        savePartition.setOnAction(e -> partitionController.sauvegarderPartition());
        
        MenuItem loadPartition = new MenuItem("Charger");
        loadPartition.setOnAction(e -> {
            partitionController.chargerPartition();
            partitionView.mettreAJourAffichage();
        });

        menuFile.getItems().addAll(savePartition, loadPartition);
        menuBar.getMenus().add(menuFile);

        HBox controls = new HBox(10);
        ComboBox<String> noteSelector = new ComboBox<>();
        noteSelector.getItems().addAll("Si grave", "Do grave", "Ré grave", "Mi grave", "Fa grave", "Sol", "La", "Si", "Do", "Ré", "Mi", "Fa", "Sol aigu", "La aigu", "Si aigu", "Do aigu", "Ré aigu", "Mi aigu", "Fa aigu");
        noteSelector.setValue("Do");

        ComboBox<String> dureeSelector = new ComboBox<>();
        dureeSelector.getItems().addAll("Noire", "Blanche", "Ronde");
        dureeSelector.setValue("Noire");

        Button addNote = new Button("Ajouter Note");
        addNote.setOnAction(e -> {
            String selectedNote = noteSelector.getValue();
            String selectedDuree = dureeSelector.getValue();
            partitionController.ajouterNote(selectedNote, selectedDuree);
            LecteurMIDI.jouerNote(new Note(selectedNote, selectedDuree,false));
            partitionView.mettreAJourAffichage();
        });
        Button addSilence = new Button("Ajouter Silence");
        addSilence.setOnAction(e -> {
            String selectedDuree = dureeSelector.getValue();
            partitionController.ajouterSilence(selectedDuree);
            partitionView.mettreAJourAffichage();
        });
        Button lirePartition = new Button("Lire la partition");
        lirePartition.setOnAction(e -> partitionController.lirePartition());

        controls.getChildren().addAll(noteSelector, dureeSelector, addNote, addSilence, lirePartition);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(partitionView);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
