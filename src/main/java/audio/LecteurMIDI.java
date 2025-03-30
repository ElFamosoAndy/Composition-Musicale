package audio;

import business.*;
import javax.sound.midi.*;

public class LecteurMIDI {

    // Thread en cours de lecture MIDI
    private static Thread playbackThread;

    // Indices de suivi pour la note et mesure en cours (utile pour l'affichage)
    public static int currentMeasureIndex = -1;
    public static int currentNoteIndex = -1;

    // Joue une note isolée
    public static void jouerNote(Note note, int tempo) {
        int noteMidi = hauteurToInt(note.getHauteur());
        int baseDuration = dureeToInt(note.getDuree()); // Durée à 60 BPM
        int adjustedDuration = (int)(baseDuration * (60.0 / tempo)); // Ajusté au tempo actuel
        if (noteMidi == -1 || baseDuration == -1) return;

        new Thread(() -> {
            try {
                Synthesizer synth = MidiSystem.getSynthesizer();
                synth.open();
                MidiChannel[] channels = synth.getChannels();
                if (!note.estSilence()) {
                    channels[0].noteOn(noteMidi, 80); // Joue la note
                }
                Thread.sleep(adjustedDuration); // Attente selon la durée
                channels[0].noteOff(noteMidi); // Arrête la note
                synth.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start(); // Lancement du thread
    }

    // Joue toute une partition
    public static void jouerPartition(Partition partition) {
        stopPlayback(); // Arrête la lecture en cours s'il y en a une
        int tempo = partition.getTempo();

        playbackThread = new Thread(() -> {
            try {
                Synthesizer synth = MidiSystem.getSynthesizer();
                synth.open();
                MidiChannel[] channels = synth.getChannels();

                for (int m = 0; m < partition.getMesures().size(); m++) {
                    currentMeasureIndex = m;
                    Mesure mesure = partition.getMesures().get(m);
                    for (int n = 0; n < mesure.getNotes().size(); n++) {
                        currentNoteIndex = n;

                        // Si on a interrompu la lecture, on sort
                        if (Thread.currentThread().isInterrupted()) break;

                        Note note = mesure.getNotes().get(n);
                        int noteMidi = hauteurToInt(note.getHauteur());
                        int baseDuration = dureeToInt(note.getDuree());
                        int adjustedDuration = (int)(baseDuration * (60.0 / tempo));

                        if (!note.estSilence()) {
                            channels[0].noteOn(noteMidi, 80);
                        }
                        Thread.sleep(adjustedDuration);
                        if (!note.estSilence()) {
                            channels[0].noteOff(noteMidi);
                        }
                    }
                }
                synth.close();
            } catch (InterruptedException e) {
                // Lecture stoppée manuellement
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Réinitialisation des indices à la fin
                currentMeasureIndex = -1;
                currentNoteIndex = -1;
                playbackThread = null;
            }
        });

        playbackThread.start();
    }

    // Arrête la lecture de la partition
    public static void stopPlayback() {
        if (playbackThread != null) {
            playbackThread.interrupt();
            playbackThread = null;
            currentMeasureIndex = -1;
            currentNoteIndex = -1;
        }
    }

    // Indique si une lecture est en cours
    public static boolean isPlaying() {
        return playbackThread != null;
    }

    // Convertit un nom de hauteur (Do, Ré...) en code MIDI
    private static int hauteurToInt(String hauteur) {
        return switch (hauteur) {
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
    }

    // Convertit une durée (Noire, Blanche...) en millisecondes à 60 BPM
    private static int dureeToInt(String duree) {
        return switch (duree) {
            case "Croche" -> 500;
            case "Noire" -> 1000;
            case "Blanche" -> 2000;
            case "Ronde" -> 4000;
            default -> -1;
        };
    }
}
