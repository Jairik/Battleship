package Project1;

import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class battleshipController implements ActionListener{
    
    private battleshipView view;
    private battleshipModel model;
    private battleshipServer server;
    
    //controller contructor calls view and model constructors
    public battleshipController() throws IOException {
        //Defining model and view
        model = new battleshipModel(); 
        char[][] userBoard = model.getUserBoard(); 
        view = new battleshipView(userBoard);

        //Getting host and connect Buttons
        JButton cButton = view.getConnectButton();
        JButton hButton = view.getHostButton();

        /* Adding action listeners for buttons, then defining them */
        System.out.println("In while loop");
        //If any of this works, dont ask me why I have absolutely no clue
        hButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Run this in the background under a different thread so the GUI can later update */
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        server = new battleshipServer(true);
                        server.Connect();
                        return null;
                    }
                    @Override
                    protected void done() {
                        view.updateMiddlePanel();
                    }
                };    
            }
        });

        hButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Run this in the background under a different thread so the GUI can later update */
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        server = new battleshipServer(false);
                        server.Connect();
                        return null;
                    }
                    @Override
                    protected void done() {
                        view.updateMiddlePanel();
                    }
                };    
            }
        });

        fireCannon();
    }
    //adds a actionlistener to every button
    public void fireCannon(){
        System.out.println("Inside Fire Cannon");
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                view.getButton(i, j).addActionListener(this);
            }
        }
    }

//button action for firing
@Override
public void actionPerformed(ActionEvent e) {
    String HitOrMiss = ""; //Initializing
    JButton clickedButton = (JButton)e.getSource();
    //while(true) {
        //finds clicked button position
        int[] position = view.buttonPosition(clickedButton);
        System.out.println(position[0] + ", " + position[1]);
        //if statement calls checkforvalidshot() from model - if true hit = X else miss = O
        if(model.checkForValidShot(position[0], position[1])){
            System.out.println("Valid");
            HitOrMiss = model.determineHit(position[0], position[1]); //updates model //checks for sinkship
            System.out.println(HitOrMiss);
            view.playSoundEffect(HitOrMiss);
            if(HitOrMiss != "X" && HitOrMiss != "O"){
                view.updateView(position[0], position[1], "X");
                //view.showGameStatus(HitOrMiss);
                view.updateLabel(HitOrMiss);
                if(model.isWin()) {
                    view.declareWinner("Player");
                    //Restart the game (?) (Last thing we implement)
                }
            }
            else {
                view.updateView(position[0], position[1], HitOrMiss);
            }
            //check if all ships sunk
            if(model.isWin()) {
                System.out.println("Game over");
                view.showGameStatus("All ships sunk");
            }
            //return
        }
        else {
            view.playSoundEffect("O");
            view.updateView(position[0], position[1], "O");
        }
        
    } 

}

