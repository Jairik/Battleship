package Project1;

/*----------------------------------------------------------------------------------------------------------
  Authors: JJ McCauley & Will Lamuth 
  Creation Date: 2/23/24
  Last Update: 3/11/24
-------------------------------------------------------------------------------------------------------------*/


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean; //Avoid uncessarily complicated callbacks

public class battleshipController implements ActionListener{
    
    private battleshipView view;
    private battleshipModel model;
    private battleshipServer server;
    private boolean rotated = false;
    //private boolean buttonClicked = false;
    //controller contructor calls view and model constructors
    public battleshipController() throws IOException {
        //Defining model and view
        boolean winner = false, turn = false, host;
        int shotPosX = -1, shotPosY = -1;
        model = new battleshipModel(); 
        char[][] userBoard = model.getUserBoard(); 
        view = new battleshipView(userBoard);
        //test button to rotate the carrier image, still scuffed
        setShipsManually();
        rotateBattleship();
        /* Establish a connection between host and client - Ships can not be modified yet and shots cannot be fired */
        establishConnection();
        view.updateMiddlePanelPlace(); //Update the middle panel for placement
        //placeShips(); <--!!All relevent functions for placing ships should go in here. If that isn't possible,
        //I can try to multithread and run whatever 2/3 functions at the same time
        server.send(model.getUserBoard());
        char oppBoard[][] = server.receiveBoard();
        model.setOppBoard(oppBoard);
        view.updateMiddlePanelPlay(); //Update the middle panel with ship status
        
        //Remove action listeners for the d-n-d ships
        readyCannons(); //add actionlisteners to the buttons
        host = server.isHost();
        turn = host; //Set the first turn to always be the host
        while(winner) {
            while(turn) {
                //Wait for the event of a shot cannon, then turn off actionEventListener (maybe?)
                //Get X & Y Positions
                //Validate X & Y Positions
                //Probably copying alot from the actionPerformed for fireCannon (or just using it w/ slight modification)
                server.send(shotPosX, shotPosY); //Send the current shot
                //CheckForWinner(userBoard)
                turn = false;
            }
            server.receiveCoordinates(); //Wait until the opposing user sends shot coordinates
            //!NECESSARY CODE!if (/*model.isWin(oppBoard)*/) //We're going to have to think out this logic
            turn = true;
        }
        
    }

    //adds a actionlistener to every button
    public void readyCannons(){
        System.out.println("Inside Fire Cannon");
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                view.getButton(i, j).addActionListener(this);
            }
        }
    }

    public void setShipsManually(){
        view.getSetButton().addActionListener(new ActionListener() {
            @SuppressWarnings("unused")
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("setship clicked");
                List<ImageInfo> imagesInfo = view.getPanelInfo();
                model.clearBoard();
                for (int i = 0; i < imagesInfo.size(); i++) {
                    ImageInfo imageInfo = imagesInfo.get(i);
                    Point coordinates = imageInfo.getCoordinates();
                    String imagePath = imageInfo.getImagePath();
                    if(imagePath == "/resources/Carrier.png" || imagePath == "/resources/carrierRotated.png"){
                        BS_Carrier cShip = new BS_Carrier(coordinates, model);
                        System.out.println("Carrier");
                    }
                    else if(imagePath == "/resources/Battleship.png" || imagePath == "/resources/battleshipRotated.png"){
                        BS_Battleship bShip = new BS_Battleship(coordinates, model);
                        System.out.println("battleship");
                    }
                    else if(imagePath == "/resources/Destroyer.png" || imagePath == "/resources/destroyerRotated.png"){
                        BS_Destroyer dShip = new BS_Destroyer(coordinates, model);
                        System.out.println("destroyer");
                    }
                    else if(imagePath == "/resources/Cruiser.png" || imagePath == "/resources/cruiserRotated.png"){
                        BS_Cruiser rShip = new BS_Cruiser(coordinates, model);
                        System.out.println("cruiser");
                    }
                    else if(imagePath == "/resources/SubmarineReSize.png" || imagePath == "/resources/submarineRotated.png"){
                        BS_Submarine sShip = new BS_Submarine(coordinates, model);
                        System.out.println("submarine");
                    }
                    System.out.println("Image " + (i + 1) + " - X: " + coordinates.getX() + ", Y: " + coordinates.getY() + ", Path: " + imagePath);
                }
                model.printBoard();
            }
        });
    }

    public void rotateBattleship(){
        view.getRotateShip().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<DraggableImage> images = view.getPanel().getImages();
                DraggableImage oldImage = images.get(0);
                if(!rotated){
                    images.set(0, new DraggableImage("/resources/carrierRotated.png", oldImage.getImageUpperLeft()));
                    rotated = true;
                }
                else{
                    images.set(0, new DraggableImage("/resources/Carrier.png", oldImage.getImageUpperLeft()));
                    rotated = false;
                }
                view.getPanel().repaint();
            }
        });
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

    void establishConnection() {
        //Getting host and connect Buttons
        JButton cButton = view.getConnectButton(); 
        JButton hButton = view.getHostButton();
        AtomicBoolean connection = new AtomicBoolean(false);
        //Adding event listeners for each of the buttons
        hButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean c = false;
                server = new battleshipServer(true);
                c = server.Connect();
                //^^ Multithread with view.createHostExternalWindow()
                connection.set(c);    
            }
        });
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean c = false;
                server = new battleshipServer(false);
                c = server.Connect();
                //view.clientErrorMessage() //May also have to multithread this
                connection.set(c);
            }
        });
        //Loop until a connection is established
        while(!(connection.get())) {
            try {
                Thread.sleep(100); //Avoid "busy-looping"
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /* Handle events of ships dragging and add event handlers for each of the buttons */
    void placeShips() {
        JButton finalizeShipPlacement = view.getFinalizePlacement();
        JButton randomPlacement = view.getRandomPlacement();
        JButton rotateCarrier = view.getRotateCarrier();
        JButton rotateBattleship = view.getRotateBattleship();
        JButton rotateCruiser = view.getRotateCruiser();
        JButton rotateSubmarine = view.getRotateSubmarine();
        JButton rotateDestroyer = view.getRotateDestroyer();
    }
}

