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
        //The rest of the actual "good" code
        model = new battleshipModel(); 
        char[][] userBoard = model.getUserBoard(); 
        view = new battleshipView(userBoard);
        //Getting host and connect Buttons
        JButton cButton = view.getConnectButton();
        JButton hButton = view.getHostButton();
        //Adding action listeners and defining them
        hButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server = new battleshipServer(true);
                view.createHostExternalWindow("127.0.0.1");
                server.Connect();
                view.updateMiddlePanel();
            }
        });
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.createConnectExternalWindow();
                server = new battleshipServer(false);
                view.updateMiddlePanel();
                server.Connect();
            }
        });

        //getMouseInput
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
            else{
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

