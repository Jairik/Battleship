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
    //private boolean buttonClicked = false;
    //controller contructor calls view and model constructors
    public battleshipController() throws IOException {
        //Defining model and view
        boolean winner = false, turn = false, host;
        int shotPosX = -1, shotPosY = -1;
        model = new battleshipModel(); 
        char[][] userBoard = model.getUserBoard(); 
        view = new battleshipView(userBoard);

        setShipsManually();
        
        //carrier button
        view.getRotateCarrierShip().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCarrier = rotateBattleship(0, "/resources/carrierRotated.png", "/resources/Carrier.png");
                
            }
        });
        //battleship button
        view.getRotateBattleShip().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateBattleship = rotateBattleship(1, "/resources/battleshipRotated.png", "/resources/Battleship.png");
                
            }
        });
        //cruiser button
        view.getRotateCruiserShip().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCruiser = rotateBattleship(2, "/resources/cruiserRotated.png", "/resources/Cruiser.png");
                
            }
        });
        //submarine button
        view.getRotateSubShip().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateSubmarine = rotateBattleship(3, "/resources/submarineRotated.png", "/resources/SubmarineReSize.png");
                
            }
        });
        //destroyer button
        view.getRotateDestroyerShip().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateDestroyer = rotateBattleship(4, "/resources/destroyerRotated.png", "/resources/Destroyer.png");
                
            }
        });
        //test button to rotate the carrier image, still scuffed
        
        //Getting host and connect Buttons
        JButton cButton = view.getConnectButton();
        JButton hButton = view.getHostButton();
        fireCannon();
        /* Adding action listeners for buttons, then defining them */

        System.out.println("Updating Panel: ");
        //view.updateMiddlePanel();


        /* We should have this all in a while loop inside a try catch statement that runs while
         * a boolean winner is false and while the connection is valid.
         */

        /*SwingUtilities.invokeLater(() ->*/ fireCannon();

        
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
            }
        });
    }

    public boolean rotateBattleship(int index, String path1, String path2){
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
                connection.set(c);    
            }
        });
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean c = false;
                server = new battleshipServer(false);
                c = server.Connect();
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

