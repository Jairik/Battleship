package Project1;

/*----------------------------------------------------------------------------------------------------------
  Authors: JJ McCauley & Will Lamuth 
  Creation Date: 2/23/24
  Last Update: 3/11/24
-------------------------------------------------------------------------------------------------------------*/


import java.io.IOException;
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
    //private boolean buttonClicked = false;
    //controller contructor calls view and model constructors
    public battleshipController() throws IOException {
        boolean winner = false, turn = false, host;
        int shotPosX = -1, shotPosY = -1;
        //Defining model and view
        model = new battleshipModel(); 
        char[][] userBoard = model.getUserBoard(); 
        view = new battleshipView(userBoard);
        establishConnection(); //Establish a connection with the user
        view.updateMiddlePanelPlace(); //Update the middle panel relevent ship placement buttons
        placeShips(); //Call the function to place the ships
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
                //Wait for the event of a shot cannon
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

//button action for firing
    @Override
    public void actionPerformed(ActionEvent e) {
        String HitOrMiss = ""; //Initializing
        JButton clickedButton = (JButton)e.getSource();
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
                //Open the messageDialougePanel thing in view while also running server.Connect
                c = server.Connect();
                connection.set(c);    
            }
        });
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean c = false;
                server = new battleshipServer(false);
                c = server.Connect();
                if(c == false) {
                    //Open a pop-up window informing the user that no host has been found
                    view.clientErrorMessage();
                }
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
    //Dev note: not sure if we can divide this out but for now this function is going to be huge
    void placeShips() {
        AtomicBoolean finalizePlacement = new AtomicBoolean(false);
        /*JButton finalizeShipPlacement = view.getFinalizePlacement();
        JButton randomPlacement = view.getRandomPlacement();
        JButton rotateCarrier = view.getRotateCarrier();
        JButton rotateBattleship = view.getRotateBattleship();
        JButton rotateCruiser = view.getRotateCruiser();
        JButton rotateSubmarine = view.getRotateSubmarine();
        JButton rotateDestroyer = view.getRotateDestroyer(); */

        //While the finalizePlacement button is not clicked
        while(!finalizePlacement.get()) {
            view.getFinalizePlacement().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("setship clicked");
                    List<ImageInfo> imagesInfo = view.getPanelInfo();
                    model.clearBoard();
                    for (int i = 0; i < imagesInfo.size(); i++) {
                        ImageInfo imageInfo = imagesInfo.get(i);
                        Point coordinates = imageInfo.getCoordinates();
                        String imagePath = imageInfo.getImagePath();
                        if(imagePath == "/resources/Carrier.png" ){
                            BS_Carrier cShip = new BS_Carrier(coordinates, model);
                            System.out.println("Carrier");
                        }
                        else if(imagePath == "/resources/Battleship.png"){
                            BS_Battleship bShip = new BS_Battleship(coordinates, model);
                            System.out.println("battleship");
                        }
                        else if(imagePath == "/resources/Destroyer.png"){
                            BS_Destroyer dShip = new BS_Destroyer(coordinates, model);
                            System.out.println("destroyer");
                        }
                        else if(imagePath == "/resources/Cruiser.png"){
                            BS_Cruiser rShip = new BS_Cruiser(coordinates, model);
                            System.out.println("cruiser");
                        }
                        else if(imagePath == "/resources/SubmarineReSize.png"){
                            BS_Submarine sShip = new BS_Submarine(coordinates, model);
                            System.out.println("submarine");
                        }
                        System.out.println("Image " + (i + 1) + " - X: " + coordinates.getX() + ", Y: " + coordinates.getY() + ", Path: " + imagePath);
                    }
                    model.printBoard();
                    finalizePlacement.set(true);
                }
            });

            //Action performed when the randomize button is placed
            view.getRandomPlacement().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.randomlySetBoard(); 
                    //Update the view so that the ships align with the board
                }
            });

            view.getRotateCarrier().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Action when rotateCarrier button is pressed
                }
            });

            view.getRotateBattleship().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Action when rotateBattleship button is pressed
                }
            });

            view.getRotateCruiser().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Action when rotateCruiser button is pressed
                }
            });

            view.getRotateSubmarine().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Action when rotateSubmarine button is pressed
                }
            });

            view.getRotateDestroyer().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Action when rotateDestroyer button is pressed
                }
            });
        }
    }
} 

