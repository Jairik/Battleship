package Project1;

/*----------------------------------------------------------------------------------------------------------
  Authors: JJ McCauley & Will Lamuth 
  Creation Date: 2/23/24
  Last Update: 3/5/24
-------------------------------------------------------------------------------------------------------------*/


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/* 
 - Implement the View component, which includes creating two boards on JFrame. 
   Grids can be implemented using JButton (2D array of JButtons, or 2D array of labels, etc.).

 - Implement the ship placement (automatic placement, Drag-n-Drop of ships).

 - Incorporate a data model object into the View (as a data member of View using composition for example)
   and use event handler to update data model object when events happen (such as clicking a grid, placing a ship through drag-n-drop).

 - Implement the controller program to test View and Model. At this stage,
   your test program should have GUI and a Data Model backend and should update the GUI and data model accordingly.
*/






public class battleshipView{
    
    private JFrame frame;
    private JPanel rightPanel; //Used for the user to shoot
    private JPanel middlePanel; //Used to store ship information and stuff
    private MyPanel leftPanel; //Used for drag & drop and displaying ships
    private JButton[][] button2;
    private JLabel[][] label;
    SoundFX soundEffects;

    /* Labels on middle panel - showcases the current state of the ships */
    JLabel carrier;
    JLabel battleShip;
    JLabel submarine;
    JLabel destroyer;
    JLabel cruiser;

    /* Button to be applied to connect button */
    JButton cancelHost;

    /* Different buttons that will be applied to the middle panel */
    JButton pushToHost;
    JButton pushToConnect;
    JButton pushToSet;
    JButton finalizeShipPlacement;
    JButton randomPlacement;
    JButton rotateCarrier;
    JButton rotateBattleship;
    JButton rotateCruiser;
    JButton rotateSubmarine;
    JButton rotateDestroyer;
    List<String> imagePaths;

    /* Font used to change labels/buttons to indicate a hit */
    Font indicatorFont = new Font("Arial", Font.BOLD, 30);

    //View constructor that builds frame, gridlayout, labels, and buttons
    battleshipView(char[][] testArr) throws IOException {

        Color seaTurqoise = new Color(108, 218, 231);

        //list of images to pass into MyPanel leftPanel
        imagePaths = new ArrayList<>();
        imagePaths.add("/resources/Carrier.png");
        imagePaths.add("/resources/Battleship.png");
        imagePaths.add("/resources/Cruiser.png");
        imagePaths.add("/resources/SubmarineReSize.png");
        imagePaths.add("/resources/Destroyer.png"); 
        /* Initialize sounds and start the game music */
        soundEffects = new SoundFX();

        /* Create the root Frame */
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 500);
        frame.setLayout(new BorderLayout());       
        frame.setLocationRelativeTo(null);

        /* Create left panel - used to display user ships and for Drag-n-Drop */
        leftPanel = new MyPanel(imagePaths);
        leftPanel.setBackground(seaTurqoise);
        leftPanel.setLayout(new GridLayout(10, 10));
        
        //Add grid labels for left of the screen
        label = new JLabel[10][10];
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                String testChar = Character.toString(testArr[i][j]);
                label[i][j] = new JLabel();
                label[i][j].setText(testChar);
                label[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                leftPanel.add(label[i][j]);
            }
        }
        
        //Setting dimensions
        leftPanel.setPreferredSize(new Dimension(500, 500));
        
        /* Create right panel - used for shooting at enemy ships */
        rightPanel = new JPanel();
        rightPanel.setBackground(seaTurqoise);
        rightPanel.setLayout(new GridLayout(10, 10));

        //adds grid of buttons to second panel
        //button1 = new JButton[10][10];
        button2 = new JButton[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton btn = new JButton();
                btn.setBackground(seaTurqoise);
                button2[i][j] = btn;
                rightPanel.add(btn);
            }
        }

        rightPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        rightPanel.setPreferredSize(new Dimension(500, 500));

        /* Create middle panel, reponsible for holding pictures of different ships */
        middlePanel = new JPanel();
        middlePanel.setBackground(Color.GRAY);
        middlePanel.setLayout(new GridLayout(8, 1)); //Setting layout for just buttons
        middlePanel.setPreferredSize(new Dimension(150, 500));


        /* Creating Initial Host and Connect buttons on JFrame */
        pushToConnect = new JButton("Connect");
        pushToHost = new JButton("Host");

        //button returns ship position
        pushToSet = new JButton("Set ships"); 
        rotateCarrier = new JButton("rotate carrier");
        rotateBattleship = new JButton("rotate battleship");
        rotateCruiser = new JButton("rotate cruiser");
        rotateDestroyer = new JButton("rotate destroyer");
        rotateSubmarine = new JButton("rotate submarine");
        // temp button to test ship setting
        /* Placing Buttons On the Middle Panel */
        middlePanel.add(pushToConnect);
        middlePanel.add(pushToHost);
        middlePanel.add(pushToSet);
        middlePanel.add(rotateCarrier);
        middlePanel.add(rotateBattleship);
        middlePanel.add(rotateCruiser);
        middlePanel.add(rotateDestroyer);
        middlePanel.add(rotateSubmarine);

        /* Placing the bottoms in the right location */
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);

        frame.setTitle("Battle-Ship-1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(1150, 500));
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

    }
    /* Never Used */
    void createConnectExternalWindow() {
        JOptionPane.showMessageDialog(frame, "Attempting to connect...", "Attempting to Connect", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /* Create a pop-up window for the host. Exiting will cancel connection  */
    void createHostExternalWindow() {
        cancelHost = new JButton("Cancel");
        /* Implementation of button, adding it to the message dialouge and adding an actionListener */
        JOptionPane.showMessageDialog(frame, "Waiting for connection...", "Awaiting Connection", JOptionPane.INFORMATION_MESSAGE);
    }

    void clientErrorMessage() {
        String message = "Error Connecting\nEnsure that Host has started the game";
        JOptionPane.showMessageDialog(frame, message, "Couldn't Connect!", JOptionPane.INFORMATION_MESSAGE);
    }

    public JButton getConnectButton() {
        return pushToConnect;
    }

    public JButton getHostButton() {
        return pushToHost;
    }

    public JButton getSetButton(){
        return pushToSet;
    }

    public List<ImageInfo> getPanelInfo(){
        return ((MyPanel) leftPanel).getImagesInfo();
    }

    public MyPanel getPanel(){
        return leftPanel;
    }

    public List<DraggableImage> getDragImage(){
        return leftPanel.getImages();
    }

    public List<String> getPathList(){
        return imagePaths;
    }

    /* Update the middle panel with ships */
    void updateMiddlePanelPlay() {
        /* Resetting the middle panel */
        middlePanel.removeAll(); //Remove the current elements from the panel
        middlePanel.setBackground(Color.GREEN); //just for testing
        middlePanel.setLayout(new GridLayout(5, 1));
        middlePanel.setPreferredSize(new Dimension(300, 100));
        
        //middle panel formatting (should change later)
        battleShip = new JLabel("Battle Ship");
        battleShip.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        middlePanel.add(battleShip);
        carrier = new JLabel("Carrier");
        carrier.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        middlePanel.add(carrier);
        cruiser = new JLabel("Cruiser");
        cruiser.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        middlePanel.add(cruiser);
        submarine = new JLabel("Sub");
        submarine.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        middlePanel.add(submarine);
        destroyer = new JLabel("Destroyer");
        destroyer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        middlePanel.add(destroyer);

        //Submit the changes to the GUI
        middlePanel.revalidate();
        middlePanel.repaint();
    }

    //called in fireCannon() method in controller. returns a button
    public JButton getButton(int row, int column) {
        return button2[row][column];
    }

    //called in controller to find clickedButton x&y position
    public int[] buttonPosition(JButton btn) {
        // Find the button's position in the array
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (button2[i][j] == btn) {
                   return new int[]{i, j};
                }
            }
        }
        return null;
    }

    //called in controller after checking if hit or miss. sets button text to X for hit or O for miss
    public void updateView(int row, int column, String HitOrMiss) {
        button2[row][column].setFont(indicatorFont);      
        button2[row][column].setText("•");
        if(HitOrMiss == "O") {
            button2[row][column].setForeground(Color.GRAY);
        }
        else {
            button2[row][column].setForeground(Color.RED);
        }
    }

    //testing
    public void showGameStatus(String message){
        JOptionPane.showMessageDialog(frame, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
    }

    /* Opens a new window declaring when a player has won */
    public void declareWinner(String message){
        JOptionPane.showMessageDialog(frame, message + " has won!", "Winner!", JOptionPane.INFORMATION_MESSAGE);
    }
    //Used in controller
    public void showConnectionError() {
        String message = "Whoops, your opponent has disconnected!\nPlease close this window to exit the game";
        JOptionPane.showMessageDialog(frame, message, "Opponent Disconnected!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void playSoundEffect(String hitOrMiss) {
        if(hitOrMiss == "O") {
            soundEffects.playMissSound();
        }
        else { //ship has been hit or sank
            soundEffects.playHitSound();
        }
    }

    /* Update the middle panel for placing ships */
    void updateMiddlePanelPlace() {
        /* Resetting the middle panel */
        middlePanel.removeAll(); //Remove the current elements from the panel
        middlePanel.setBackground(Color.GRAY); //just for testing
        middlePanel.setLayout(new GridLayout(7, 1));
        middlePanel.setPreferredSize(new Dimension(300, 100));

        /* Creating buttons to finalize the ship placement, randomizing ships, and rotating each ship */
        finalizeShipPlacement = new JButton("Finalize Placement");
        finalizeShipPlacement.setBackground(new Color(255, 82, 82)); //Set the button color to a light-ish red
        randomPlacement = new JButton("Randomize");
        rotateCarrier = new JButton("Rotate Carrier");
        rotateBattleship = new JButton("Rotate Battleship");
        rotateCruiser = new JButton("Rotate Cruiser");
        rotateSubmarine = new JButton("Rotate Submarine");
        rotateDestroyer = new JButton("Rotate Destroyer");

        /* Add the buttons to the panel */
        middlePanel.add(finalizeShipPlacement);
        middlePanel.add(randomPlacement);
        middlePanel.add(rotateCarrier);
        middlePanel.add(rotateBattleship);
        middlePanel.add(rotateCruiser);
        middlePanel.add(rotateSubmarine);
        middlePanel.add(rotateDestroyer);

        //Submit the changes to the GUI
        middlePanel.revalidate();
        middlePanel.repaint();
    }   

    /* Getter functions for the controller that returns the button */
    JButton getFinalizePlacement() {
        return finalizeShipPlacement;
    }

    JButton getRandomPlacement() {
        return randomPlacement;
    }

    JButton getRotateCarrier() {
        return rotateCarrier;
    }

    JButton getRotateBattleship() {
        return rotateBattleship;
    }

    JButton getRotateCruiser() {
        return rotateCruiser;
    }

    JButton getRotateSubmarine() {
        return rotateSubmarine;
    }

    JButton getRotateDestroyer() {
        return rotateDestroyer;
    }

    public boolean getPlayAgain() {
        PlayAgainWindow p = new PlayAgainWindow();
        return p.getInput();
    }

    /* Called by controller to delete the frame, used to end the program */
    void forceCloseProg() {
        frame.dispose(); //close the current JFrame
    }

    void receiveShot(int x, int y, String HitOrMiss) {
        label[x][y].setFont(indicatorFont);      
        label[x][y].setText("•");
        if(HitOrMiss == "O") {
            button2[x][y].setForeground(Color.GRAY);
        }
        else {
            button2[x][y].setForeground(Color.RED);
        }
    }

    public void updateLeftPanel(){
        JPanel randomPanel = new JPanel();
        randomPanel.setLayout(new GridLayout(10, 10));

        JLabel[][] grid = new JLabel[10][10];
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JLabel lbl = new JLabel();
                grid[i][j] = lbl;
                grid[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }

    }
}

/* Create an external window, asking the user if they would like to play again */
class PlayAgainWindow {
    private JFrame pAgainWindow;
    //Initializing a variable signifying the user's choice, with -1 stating that user has not made one yet
    int pAgain = -1; 

    public PlayAgainWindow() {
        pAgainWindow = new JFrame("Play Again?");

        pAgainWindow.setSize(500, 200);
        pAgainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create components
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Would you like to play again?");
        JButton yesButton = new JButton("Yes!");
        JButton noButton = new JButton("No :(");

        // Add components to panel
        panel.add(label);
        panel.add(yesButton);
        panel.add(noButton);

        // Add panel to window
        pAgainWindow.add(panel);

        // Add action listeners to buttons
        yesButton.addActionListener(e -> {
            pAgain = 1;
            
        });

        noButton.addActionListener(e -> {
            pAgain = 0;
        });

        pAgainWindow.setLocationRelativeTo(null); // Center the window
    }

    boolean getInput() {
        boolean choice = true;
        while(true) { //needed to avoid error
            try {
                Thread.sleep(500);
                while(pAgain != -1) {
                    if(pAgain == 0) {
                        choice = false;
                    }
                    pAgainWindow.dispose();
                    return choice;
                }
            }
            catch (InterruptedException e) {
                return false;
            }
        }
    }
}










    