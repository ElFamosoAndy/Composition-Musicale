package audio;

import business.*;

import javax.sound.midi.*;

public class LecteurMIDI {

    // Thread de lecture en cours
    private static Thread playbackThread;

    public static void jouerNote(Note note, int tempo) {
        int noteMidi = hauteurToInt(note.getHauteur());
        int baseDuration = dureeToInt(note.getDuree()); // durée pour 60 BPM
        int adjustedDuration = (int)(baseDuration * (60.0 / tempo));
        if (noteMidi == -1 || baseDuration == -1) return;

        new Thread(() -> {
            try {
                Synthesizer synth = MidiSystem.getSynthesizer();
                synth.open();
                MidiChannel[] channels = synth.getChannels();
                if (!note.estSilence()) {
                    channels[0].noteOn(noteMidi, 80);
                }
                Thread.sleep(adjustedDuration);
                channels[0].noteOff(noteMidi);
                synth.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void jouerPartition(Partition partition) {
        stopPlayback(); // S'assurer qu'aucune lecture n'est en cours
        int tempo = partition.getTempo();
        playbackThread = new Thread(() -> {
            try {
                Synthesizer synth = MidiSystem.getSynthesizer();
                synth.open();
                MidiChannel[] channels = synth.getChannels();
                outer:
                for (Mesure mesure : partition.getMesures()) {
                    for (Note note : mesure.getNotes()) {
                        if (Thread.currentThread().isInterrupted()) {
                            break outer;
                        }
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
                // Lecture interrompue
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                playbackThread = null;
            }
        });
        playbackThread.start();
    }

    public static void stopPlayback() {
        if (playbackThread != null) {
            playbackThread.interrupt();
            playbackThread = null;
        }
    }
    
    public static boolean isPlaying() {
        return playbackThread != null;
    }

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
