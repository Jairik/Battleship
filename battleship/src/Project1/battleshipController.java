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
        //Defining model and view
        model = new battleshipModel(); 
        char[][] userBoard = model.getUserBoard(); 
        view = new battleshipView(userBoard);
        setShipsManually();

        view.getRotateShip().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("Rotate carrier");
                List<DraggableImage> images = view.getDragImage();
                if (!images.isEmpty()) {
                    DraggableImage firstImage = images.get(0);
                    firstImage.rotateImage(view.getPanel());
                }
            }
        });
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

