package audio;

import business.Note;
import javax.sound.midi.*;
import java.util.List;

public class LecteurMIDI {

    public static void jouerNote(Note note) {
        int noteMidi = hauteurToInt(note.getHauteur());
        int dureeMidi = dureeToInt(note.getDuree());

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

    public static void jouerPartition(List<Note> notes) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();

            for (Note note : notes) {
                int hauteur = hauteurToInt(note.getHauteur());
                int duree = dureeToInt(note.getDuree());

                channels[0].noteOn(hauteur, 80);
                Thread.sleep(duree);
                channels[0].noteOff(hauteur);
            }

            synth.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            case "Noire" -> 1000;
            case "Blanche" -> 2000;
            case "Ronde" -> 4000;
            default -> -1;
        };
    }
}
