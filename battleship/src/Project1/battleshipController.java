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
    boolean clicked = true;
    boolean finalizeClicked = false;
    boolean donePlacing = false;
    boolean manualClicked = false;
    boolean modelSet;
    OppPlayer opponent;
    

    //private boolean buttonClicked = false;
    //controller contructor calls view and model constructors
    public battleshipController() throws IOException {
        char oppBoard[][]; //Initialize a null board for the opponent
        //Defining model and view
        boolean turn = false, pAgain = true, host, opponentPAgain;
        model = new battleshipModel(); 
        char[][] userBoard = model.getUserBoard(); 
        view = new battleshipView();

        // initialized opponent
        opponent = new OppPlayer(); 
        if(pAgain) {
            view.updateMiddlePanelPlace();//Update the middle panel for placement
            randomizePanel(); // button function to randomize ship
            //manualPanel();
            rotateShipButtons();
            setShipsManually();
            setShipsRandomly();
            while(!manualClicked) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Out of rotateShips function");
            readyCannons();
        
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

    //function sets action for button to set ships manually
    public void setShipsManually(){
        view.getFinalizePlacement().addActionListener(new ActionListener() {
            @SuppressWarnings("unused")
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("setship clicked");
                List<ImageInfo> imagesInfo = view.getPanelInfo();
                model.clearBoard();
                //iterates through List, assigning coordinate, imagepath,
                //sends coordinates to corresponding ship class by comparing image path
                for (int i = 0; i < imagesInfo.size(); i++) {
                    ImageInfo imageInfo = imagesInfo.get(i);
                    Point coordinates = imageInfo.getCoordinates();
                    String imagePath = imageInfo.getImagePath();
                    if(imagePath.equals("/resources/Carrier.png") || imagePath.equals("/resources/carrierRotated.png")){
                        cShip = new BS_Carrier(coordinates, model, rotateCarrier);
                        System.out.println("Carrier");
                    }
                    else if(imagePath.equals("/resources/Battleship.png") || imagePath.equals("/resources/battleshipRotated.png")){
                        bShip = new BS_Battleship(coordinates, model, rotateBattleship);
                        System.out.println("battleship");
                    }
                    else if(imagePath.equals("/resources/Destroyer.png") || imagePath.equals("/resources/destroyerRotated.png")){
                        dShip = new BS_Destroyer(coordinates, model, rotateDestroyer);
                        System.out.println("destroyer");
                    }
                    else if(imagePath.equals("/resources/Cruiser.png") || imagePath.equals("/resources/cruiserRotated.png")){
                        rShip = new BS_Cruiser(coordinates, model, rotateCruiser);
                        System.out.println("cruiser");
                    }
                    else if(imagePath.equals("/resources/SubmarineReSize.png") || imagePath.equals("/resources/submarineRotated.png")){
                        sShip = new BS_Submarine(coordinates, model, rotateSubmarine);
                        System.out.println("submarine");
                    }
                    System.out.println("Image " + (i + 1) + " - X: " + coordinates.getX() + ", Y: " + coordinates.getY() + ", Path: " + imagePath);
                }
                model.printBoard();
                //sets images to not move after button clicked
                view.getPanel().setImagesMovable(false);
                modelSet = true;
                model.setModelBoolean(modelSet);
                donePlacing = true;
                manualClicked = true;
                //updates middle panel after finalize ships
                view.updateMiddlePanelPlay();
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

    /* Adding action listeners for the "rotate" buttons */
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

    //handles button function for randomly setting board
    public void randomizePanel(){
        view.getRandomPlacement().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.randomlySetBoard();
                //replace left panel with grid
                System.out.println("random ship clicked");
                model.printBoard();
                char[][] randomBoard = model.getUserBoard();
                //view.updateMiddlePanel3();
                System.out.print("random model set");

                view.updateLeftPanelRandom(randomBoard);
            }
        });
    }

    //handles button function for manual placement
    public void manualPanel(){
        view.getManualButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //view.updateLeftPanelManual();
                //manualClicked = true;
            }
        });
    }

    public void setShipsRandomly(){
        view.getfinalizeRandomButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //manualClicked = true;
                manualClicked = true;
                modelSet = true;
                model.setModelBoolean(modelSet);
                view.updateMiddlePanelPlay();
                
            }
        });
    }


    /* Action Listeners for Buttons - gets the shot, determine if it is valid, then return the position*/
    @Override
    public void actionPerformed(ActionEvent e) {
        String HitOrMiss = ""; //Initializing
        JButton clickedButton = (JButton)e.getSource();
        
        //if model isnt set 
        if(model.modelIsSet()){
            System.out.println("model set now ready to fire");
            //finds clicked button position
            int[] position = view.buttonPosition(clickedButton);
            System.out.println(position[1] + ", " + position[0]);
            //if statement calls checkforvalidshot() from opponent - if true hit = X else miss = O
            if(opponent.checkForValidShot(position[0], position[1])){
                HitOrMiss = opponent.determineHit(position[0], position[1]); 
                System.out.println(HitOrMiss);
                view.playSoundEffect(HitOrMiss);
                //If HitOrMiss is neither "X" or "O" it signifies ship has been sunk
                if(HitOrMiss != "X" && HitOrMiss != "O"){
                    view.updateView(position[0], position[1], "X");
                    System.out.println(HitOrMiss + " has sunk");
                    view.updateLabel(HitOrMiss);
                    if(opponent.isWin()) {
                        view.updateView(position[0], position[1], "X");
                        view.showGameStatus("All ships sunk User won");
                        System.out.println("Game over ships sunk");
                        opponent.printBoard();
                    }
                    if(model.isWinUser()){
                        view.updateView(position[0], position[1], "W");
                        view.showGameStatus("All ships sunk Opponent won");
                    }
                    opponent.printBoard();
                }
                else {
                    view.updateView(position[0], position[1], HitOrMiss);
                    System.out.println("valid shot");
                    opponent.printBoard();
                }
                opponent.opponentShoot(model.getUserBoard(), 'X'); //opponent shoots after
                System.out.println("User board");
                model.printBoard();
            }
            else {
                view.playSoundEffect("O");
                shotPosX = position[0];
                shotPosY = position[1];
                System.out.println("not valid shot");
                opponent.printBoard();
            }
        }
        else{
            System.out.print("Board not set");
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

