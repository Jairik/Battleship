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
    private boolean rotated = false;
    BS_Carrier cShip;
    BS_Battleship bShip;
    BS_Destroyer dShip;
    BS_Cruiser rShip;
    BS_Submarine sShip;
    boolean rotateCarrier;
    boolean rotateBattleship;
    boolean rotateCruiser;
    boolean rotateSubmarine;
    boolean rotateDestroyer;
    int shotPosX = -1, shotPosY = -1;
    boolean winner = false;

    //private boolean buttonClicked = false;
    //controller contructor calls view and model constructors
    public battleshipController() throws IOException {
        //Defining model and view
        boolean turn = false, pAgain = true, host, opponentPAgain;
        int[] cords = new int[2];
        model = new battleshipModel(); 
        char[][] userBoard = model.getUserBoard(); 
        view = new battleshipView(userBoard);
        //test button to rotate the carrier image, still scuffed
        //rotateShipButtons(); <----XXX
        /* Establish a connection between host and client - Ships can not be modified yet and shots cannot be fired */
        establishConnection();
        while(pAgain) {
            view.updateMiddlePanelPlace(); //Update the middle panel for placement
            rotateShipButtons();
            server.send(model.getUserBoard());
            char oppBoard[][] = server.receiveBoard();
            model.setOppBoard(oppBoard);
            view.updateMiddlePanelPlay(); //Update the middle panel with ship status
            //!Remove action listeners for the d-n-d ships!
            host = server.isHost();
            turn = host; //Set the first turn to always be the host
            gameLoop: //Assigning name to outermost loop so we can later break it
            while(!winner) {
                /* Shoot shot, then wait to receive input from the other player */
                while(turn) {
                    shotPosX = -1; //Adding signal value that shot has not yet been taken 
                    shotPosY = -1;
                    readyCannons(); //add actionlisteners to the buttons
                    //Looping until shotPosX is set to valid number (set in action listeners for buttons)
                    while(shotPosX == -1) {
                        try {
                            Thread.sleep(80); //Avoid some unnecessary work
                        } catch (InterruptedException e) {
                            System.out.println("Thread error????????");
                        }
                    }
                    server.send(shotPosX, shotPosY); //Send the current shot
                    disarmCannons();
                    if(winner) {
                        break gameLoop;
                    }
                    turn = false;
                }
                if(!server.checkConnection()) {
                    exitGame(); //Display a window saying that the game is disconected, then exiting the program
                }
                cords = server.receiveCoordinates(); //Wait until the opposing user sends shot coordinates
                String HorM = model.determineHitUserBoard(cords[0], cords[1]);
                view.receiveShot(cords[0], cords[1], HorM);
                view.playSoundEffect(HorM);
                if(model.isWinOpponent()) { //Opponent wins
                    break gameLoop;
                }
                turn = true;
            }
            pAgain = view.getPlayAgain();
            server.send(pAgain);
            opponentPAgain = server.receivePAgain();
            if(!pAgain || !opponentPAgain) {
                pAgain = false;
            }
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

    // Method to remove all action listeners from the buttons
    public void disarmCannons() {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                view.getButton(i, j).removeActionListener(this);
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
                    if(imagePath.equals("/resources/Carrier.png") || imagePath.equals("/resources/carrierRotated.png")){
                        cShip = new BS_Carrier(coordinates, model, rotateCarrier);
                        //cShip.getRotateStatus(rotateCarrier);
                        System.out.println("Carrier");
                    }
                    else if(imagePath.equals("/resources/Battleship.png") || imagePath.equals("/resources/battleshipRotated.png")){
                        bShip = new BS_Battleship(coordinates, model, rotateBattleship);
                        //bShip.getRotateStatus(rotateBattleship);
                        System.out.println("battleship");
                    }
                    else if(imagePath.equals("/resources/Destroyer.png") || imagePath.equals("/resources/destroyerRotated.png")){
                        dShip = new BS_Destroyer(coordinates, model, rotateDestroyer);
                        //dShip.getRotateStatus(rotateDestroyer);
                        System.out.println("destroyer");
                    }
                    else if(imagePath.equals("/resources/Cruiser.png") || imagePath.equals("/resources/cruiserRotated.png")){
                        rShip = new BS_Cruiser(coordinates, model, rotateCruiser);
                        //rShip.getRotateStatus(rotateCruiser);
                        System.out.println("cruiser");
                    }
                    else if(imagePath.equals("/resources/SubmarineReSize.png") || imagePath.equals("/resources/submarineRotated.png")){
                        sShip = new BS_Submarine(coordinates, model, rotateSubmarine);
                        //sShip.getRotateStatus(rotateSubmarine);
                        System.out.println("submarine");
                    }
                    System.out.println("Image " + (i + 1) + " - X: " + coordinates.getX() + ", Y: " + coordinates.getY() + ", Path: " + imagePath);
                }
                model.printBoard();
                view.getPanel().setImagesMovable(false);
            }
        });
    }

    public boolean rotateBattleship(int index, String path1, String path2) {
        List<DraggableImage> images = view.getPanel().getImages();
        DraggableImage oldImage = images.get(index);
        if(!rotated){
            images.set(index, new DraggableImage(path1, oldImage.getImageUpperLeft()));
            rotated = true;
        }
        else{
            images.set(index, new DraggableImage(path2, oldImage.getImageUpperLeft()));
            rotated = false;
        }
        view.getPanel().repaint();
        return rotated;
    }

    public void rotateShipButtons(){
        view.getRotateCarrier().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCarrier = rotateBattleship(0, "/resources/carrierRotated.png", "/resources/Carrier.png");
            }
        });
        //battleship button
        view.getRotateBattleship().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateBattleship = rotateBattleship(1, "/resources/battleshipRotated.png", "/resources/Battleship.png");
            }
        });
        //cruiser button
        view.getRotateCruiser().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCruiser = rotateBattleship(2, "/resources/cruiserRotated.png", "/resources/Cruiser.png");
            }
        });
        //submarine button
        view.getRotateSubmarine().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateSubmarine = rotateBattleship(3, "/resources/submarineRotated.png", "/resources/SubmarineReSize.png"); 
            }
        });
        //destroyer button
        view.getRotateDestroyer().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateDestroyer = rotateBattleship(4, "/resources/destroyerRotated.png", "/resources/Destroyer.png"); 
            }
        });
    }

    public void randomizePanel(){
        view.getRandomPlacement().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.randomlySetBoard();
                //replace left panel with grid
                view.updateLeftPanel();
            }
        });
    }
    
    
    

    /* Action Listeners for Buttons - gets the shot, determine if it is valid, then return the position*/
    @Override
    public void actionPerformed(ActionEvent e) {
        String HitOrMiss = ""; //Initializing
        JButton clickedButton = (JButton)e.getSource();
        //finds clicked button position
        int[] position = view.buttonPosition(clickedButton);
        System.out.println(position[0] + ", " + position[1]);
        //if statement calls checkforvalidshot() from model - if true hit = X else miss = O
        if(model.checkForValidShot(position[0], position[1])){
            HitOrMiss = model.determineHitOpponentBoard(position[0], position[1]); //check opponent board
            view.playSoundEffect(HitOrMiss);

            //If HitOrMiss is neither "X" or "O" it signies ship has been sunk
            if(HitOrMiss != "X" && HitOrMiss != "O"){
                view.updateView(position[0], position[1], "X");
                //view.showGameStatus(HitOrMiss);
                //view.updateLabel(HitOrMiss);
            }
            else { //THIS is probably the issue
                view.updateView(position[0], position[1], HitOrMiss);
            }
            //check if all ships sunk
            if(model.isWinOpponent()) {
                System.out.println("Game over");
                //view.showGameStatus("All ships sunk"); //May be needed?
            }
            //return
        }
        else {
            view.playSoundEffect("O");
            view.updateView(position[0], position[1], "O");
            shotPosX = position[0];
            shotPosY = position[1];
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
                if(!c) {
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

    /* Close the JFrame and exit the game - in the event ofg  */
    void exitGame() {
        view.forceCloseProg();
        System.exit(0); //Forcibly end the program
    }
}

