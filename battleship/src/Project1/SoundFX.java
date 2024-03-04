package Project1;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/*--Sound-Effects--*/
public class SoundFX {
    //Variables that holds all of the sounds
    Clip missSound;
    Clip hitSound;
    Clip gameMusic;
    //Constructor to initialize all sounds w/ filepaths
    SoundFX() {
        gameMusic = getClip("/resources/Adventure & Pirate (Royalty Free Music) - THREE SHEETS TO THE WIND by Scott Buckley.wav");
        playGameMusic(); //COMMENT OUT THIS LINE TO STOP MUSIC
    }
    //When called, will play an water splash sound effect for misses
    void playMissSound() {
        missSound = getClip("/resources/Big Water Splash Sound Effect.wav");
        missSound.start();
    }

    //When called, will play an explosion sound effect for hits
    void playHitSound() {
        hitSound = getClip("/resources/EXPLOSION SOUND EFFECT (No Copyright).wav");
        hitSound.start();
    }

    //When called, will start the game music and will loop it until the program is closed
    void playGameMusic() {
        //Reduce the volume
        FloatControl gainControl = (FloatControl) gameMusic.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-40.0f);
        //Start playing the music
        gameMusic.loop(Clip.LOOP_CONTINUOUSLY); //Will continously loop the song when it ends
        gameMusic.start();
    } 

    /*Given a filename, will access the "resources" folder within the src folder and return the
     * file with the matching fileName. Then, it will convert to an audio clip data type and return
     * it. This is used as a helper function in the constructor.
     */
    Clip getClip(String filename) {
        try {
            URL soundFileURL = SoundFX.class.getResource(filename);
            if(soundFileURL == null) {
                System.err.println("Sound file not found: " + filename);
                return null;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFileURL);
            Clip soundClip = AudioSystem.getClip();
            soundClip.open(audioIn);
            return soundClip;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch(LineUnavailableException e) {
            System.err.println("Unable to open audio clip");
            return null;
        }
        catch(UnsupportedAudioFileException e) {
            System.err.println("Unsopported Audio File Type");
            return null;
        }

    }
}
