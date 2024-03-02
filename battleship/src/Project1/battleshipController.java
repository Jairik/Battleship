package Project1;

import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.File;
import javax.swing.*;


public class battleshipController implements ActionListener{
    
    private battleshipView view;
    private battleshipModel model;
    
    //controllere contructor calls view and model constructors
    public battleshipController() throws IOException {
        view = new battleshipView();
        model = new battleshipModel();
        //playGameMusic();
        fireCannon();
        
        
    }
    //adds a actionlistener to every button
    public void fireCannon(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                view.getButton(i, j).addActionListener(this);
            }
        }
    }

//button action for firing
@Override
public void actionPerformed(ActionEvent e) {
    JButton clickedButton = (JButton)e.getSource();
        //finds clicked button position
        int[] position = view.buttonPosition(clickedButton);
        String HitOrMiss = "";
        //if statement calls checkforvalidshot() from model if true hit = X else miss = O
        if(model.checkForValidShot(position[0], position[1])){
            HitOrMiss = "X";
            if(HitOrMiss == "X") {
                //playHitSound();
            }
            else {
                //playMissSound();
            }
            view.updateView(position[0], position[1], HitOrMiss);
            model.updateModel(position[0], position[1], HitOrMiss); // updates the model
            if(model.shipStatus()){ // checks if ship sank. 
                view.showGameStatus(); // view function displays message
            }
        }
        else{
            HitOrMiss = "O";
        }
        //updates board accordingly in view
        view.updateView(position[0], position[1], HitOrMiss); 
    } 
    
    /*--Sound-Effects--
    //There is a way we can package this all up but its likely easier if we get all of our images and relevent
    //Files first. For Testing, we can leave this commented out
    //When called, will play an water splash sound effect for misses
    void playMissSound() {
        try {
            String filename = "C:\\Users\\jairi\\OneDrive\\Desktop\\COSC330-Battleship-\\COSC330-Battleship-\\battleship\\resources\\Big Water Splash Sound Effect.wav";
            File missSoundEffect = new File(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(missSoundEffect);
            Clip missSoundClip = AudioSystem.getClip();
            missSoundClip.open(audioIn);
            missSoundClip.start();
        }
        catch (Exception e) {
            //Can do whatever we just need a try catch statement
        }
    }

    //When called, will play an explosion sound effect for hits
    void playHitSound() {
        try {
            String filename = "C:\\Users\\jairi\\OneDrive\\Desktop\\COSC330-Battleship-\\COSC330-Battleship-\\battleship\\resources\\EXPLOSION SOUND EFFECT (No Copyright).wav";
            File hitSoundEffect = new File(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(hitSoundEffect);
            Clip hitSoundClip = AudioSystem.getClip();
            hitSoundClip.open(audioIn);
            hitSoundClip.start();
        }
        catch (Exception e) {
            //Can do whatever we just need a try catch statement
        }
    }

    //When called, will start the game music and will loop indefinitely
    void playGameMusic() {
        try {
            String filename = ""; //Can be whatever music, just must be converted to WAV file first
            File gameMusic = new File(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(gameMusic);
            Clip gameMusicClip = AudioSystem.getClip();
            gameMusicClip.open(audioIn);
            gameMusicClip.loop(Clip.LOOP_CONTINUOUSLY); //Will continously loop the song when it ends
            gameMusicClip.start();
        }
        catch (Exception e) {
            //Can do whatever we just need a try catch statement
        }
    } */
}

