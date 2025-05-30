package ui;

// Importations JavaFX pour l'interface graphique
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// Importations standards Java et AWT
import java.io.File;

// Importations internes du projet
import audio.LecteurMIDI;
import business.Note;
import controller.PartitionController;
import data.GestionFichier;

public class MainApp extends Application {
    // Contrôleur central de la partition
    private PartitionController partitionController;
    // Vue graphique de la partition (canvas personnalisé)
    private PartitionView partitionView;
    // Conteneur principal contenant toutes les infos visibles (titre, auteur, tempo, notes...)
    private VBox partitionContainer;

    @Override
    public void start(Stage primaryStage) {
        // Initialisation du contrôleur et de la vue
        partitionController = new PartitionController();
        partitionView = new PartitionView(partitionController);
        partitionView.mettreAJourAffichage();

        // Champ de texte pour le nom de la partition
        TextField partitionNameField = new TextField(partitionController.getPartition().getMetadonnes().getNom());
        partitionNameField.setMaxWidth(300);
        partitionNameField.setAlignment(Pos.CENTER);
        partitionNameField.setStyle("-fx-font-size: 32px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        partitionNameField.setOnAction(e -> {
            partitionController.getPartition().getMetadonnes().setNom(partitionNameField.getText());
            primaryStage.setTitle(partitionNameField.getText());
        });
        partitionNameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                partitionController.getPartition().getMetadonnes().setNom(partitionNameField.getText());
                primaryStage.setTitle(partitionNameField.getText());
            }
        });

        // Champ pour saisir l'auteur (compositeur)
        TextField authorField = new TextField(
            partitionController.getPartition().getMetadonnes().getCompositeur() != null ?
            partitionController.getPartition().getMetadonnes().getCompositeur() : ""
        );
        authorField.setMaxWidth(300);
        authorField.setAlignment(Pos.CENTER);
        authorField.setPromptText("Auteur");
        authorField.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        authorField.setOnAction(e -> {
            partitionController.getPartition().getMetadonnes().setCompositeur(authorField.getText());
        });
        authorField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                partitionController.getPartition().getMetadonnes().setCompositeur(authorField.getText());
            }
        });

        primaryStage.setTitle(partitionController.getPartition().getMetadonnes().getNom());

        // Contrôle de tempo via un Spinner
        Label tempoLabel = new Label("Tempo :");
        Spinner<Integer> tempoSpinner = new Spinner<>(40, 200, partitionController.getPartition().getTempo());

        // Style pour masquer la bordure du Spinner ET de son éditeur
        tempoSpinner.setStyle("-fx-font-size: 24px;"
                + "-fx-background-color: transparent; "
                + "-fx-border-color: transparent; "
                + "-fx-focus-color: transparent; "
                + "-fx-faint-focus-color: transparent; "
                + "-fx-padding: 0;");
        tempoSpinner.getEditor().setStyle("-fx-background-color: transparent; "
                + "-fx-border-color: transparent; "
                + "-fx-focus-color: transparent; "
                + "-fx-faint-focus-color: transparent; "
                + "-fx-padding: 0;");

        tempoSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            partitionController.getPartition().setTempo(newValue);
        });

        HBox tempoBox = new HBox(10, tempoLabel, tempoSpinner);
        tempoBox.setAlignment(Pos.CENTER);

        // Création du conteneur qui regroupe le titre, l'auteur, le contrôle de tempo et la partition
        partitionContainer = new VBox(10);
        partitionContainer.getChildren().addAll(partitionNameField, authorField, tempoBox, partitionView);
        partitionContainer.setAlignment(Pos.TOP_CENTER);

        // Menu "Fichier"
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Fichier");

        // "Enregistrer"
        MenuItem savePartition = new MenuItem("Enregistrer");
        savePartition.setOnAction(e -> {
            String currentPath = partitionController.getCurrentFilePath();
            if (currentPath == null || !(new File(currentPath).exists())) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Enregistrer la partition sous");
                File defaultDir = new File("partitions");
                if (!defaultDir.exists()) {
                    defaultDir.mkdirs();
                }
                fileChooser.setInitialDirectory(defaultDir);
                String sanitizedName = GestionFichier.sanitizeFilename(partitionController.getPartition().getMetadonnes().getNom());
                if (sanitizedName.isEmpty()) {
                    sanitizedName = "NouvellePartition";
                }
                fileChooser.setInitialFileName(sanitizedName + ".json");
                File file = fileChooser.showSaveDialog(primaryStage);
                if (file != null) {
                    partitionController.sauvegarderPartition(file.getAbsolutePath());
                }
            } else {
                partitionController.sauvegarderPartition();
            }
        });
        
        // "Enregistrer sous"
        MenuItem savePartitionAs = new MenuItem("Enregistrer sous");
        savePartitionAs.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la partition sous");
            File defaultDir = new File("partitions");
            if (!defaultDir.exists()) {
                defaultDir.mkdirs();
            }
            fileChooser.setInitialDirectory(defaultDir);
            String sanitizedName = GestionFichier.sanitizeFilename(partitionController.getPartition().getMetadonnes().getNom());
            if (sanitizedName.isEmpty()) {
                sanitizedName = "NouvellePartition";
            }
            fileChooser.setInitialFileName(sanitizedName + ".json");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                partitionController.sauvegarderPartition(file.getAbsolutePath());
            }
        });
        
        // "Ouvrir"
        MenuItem openPartition = new MenuItem("Ouvrir");
        openPartition.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Ouvrir la partition");
            File defaultDir = new File("partitions");
            if (!defaultDir.exists()) {
                defaultDir.mkdirs();
            }
            fileChooser.setInitialDirectory(defaultDir);
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers JSON (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                partitionController.chargerPartition(file.getAbsolutePath());
                partitionNameField.setText(partitionController.getPartition().getMetadonnes().getNom());
                authorField.setText(partitionController.getPartition().getMetadonnes().getCompositeur());
                tempoSpinner.getValueFactory().setValue(partitionController.getPartition().getTempo());
                partitionView.mettreAJourAffichage();
            }
        });
        
        // "Exporter en PDF"
        MenuItem exportPDF = new MenuItem("Exporter en PDF");
        exportPDF.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exporter la partition en PDF");
            // Répertoire par défaut : racine de l'application
            fileChooser.setInitialDirectory(new File("."));
            String sanitizedName = GestionFichier.sanitizeFilename(partitionController.getPartition().getMetadonnes().getNom());
            if (sanitizedName.isEmpty()) {
                sanitizedName = "NouvellePartition";
            }
            fileChooser.setInitialFileName(sanitizedName + ".pdf");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                GestionFichier.exporterEnPDF(partitionContainer, file);
            }
        });
        
        menuFile.getItems().addAll(savePartition, savePartitionAs, openPartition, exportPDF);
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
            LecteurMIDI.jouerNote(new Note(selectedNote, selectedDuree, false), partitionController.getPartition().getTempo());
            partitionView.mettreAJourAffichage();
        });

        Button addSilence = new Button("Ajouter Silence");
        addSilence.setOnAction(e -> {
            String selectedDuree = dureeSelector.getValue();
            partitionController.ajouterSilence(selectedDuree);
            partitionView.mettreAJourAffichage();
        });
        
        // Bouton pour revenir en arrière
        Button undoButton = new Button("Revenir en arrière");
        undoButton.setOnAction(e -> {
            partitionController.revenirEnArriere();
            partitionView.mettreAJourAffichage();
        });
        
        // Bouton pour lire/arrêter la partition avec synchronisation de l'état
        Button playToggleButton = new Button("Lire la partition");
        playToggleButton.setOnAction(e -> {
            if (playToggleButton.getText().equals("Lire la partition")) {
                partitionController.lirePartition();
                partitionView.startAnimation();
                playToggleButton.setText("Arrêter la lecture");
                new Thread(() -> {
                    while (LecteurMIDI.isPlaying()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                    Platform.runLater(() -> {
                        partitionView.stopAnimation();
                        playToggleButton.setText("Lire la partition");
                    });
                }).start();
            } else {
                LecteurMIDI.stopPlayback();
                partitionView.stopAnimation();
                playToggleButton.setText("Lire la partition");
            }
        });

        controls.getChildren().addAll(noteSelector, dureeSelector, addNote, addSilence, undoButton, playToggleButton);

        ScrollPane scrollPane = new ScrollPane(partitionContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(scrollPane);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
