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

    JLabel carrier;
    JLabel battleShip;
    JLabel submarine;
    JLabel destroyer;
    JLabel cruiser;

    JButton pushToHost;
    JButton pushToConnect;
    JButton pushToSet;
    JButton finalizeShipPlacement;
    JButton randomPlacement;
    JButton rotatePlacement;

    //View constructor that builds frame, gridlayout, labels, and buttons
    battleshipView(char[][] testArr) throws IOException{

        //list of images to pass into MyPanel leftPanel
        List<String> imagePaths = new ArrayList<>();
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
        //MyPanel carrierPanel = new MyPanel("/resources/BShip_Carrier.png", findShipPosition(testArr, 'c'));
        //MyPanel battleshipPanel = new MyPanel("/resources/BShip_Battleship_NONRESIZE.png", findShipPosition(testArr, 'c'));
        //leftPanel.add(carrierPanel);
        //leftPanel.add(battleshipPanel);
        leftPanel.setBackground(Color.BLUE);
        leftPanel.setLayout(new GridLayout(10, 10));
        
        //Add grid labels for bottom of the screen
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
        rightPanel.setBackground(Color.BLUE);
        rightPanel.setLayout(new GridLayout(10, 10));

        //adds grid of buttons to second panel
        //button1 = new JButton[10][10];
        button2 = new JButton[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton btn = new JButton();
                button2[i][j] = btn;
                rightPanel.add(btn);
            }
        }

        rightPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        rightPanel.setPreferredSize(new Dimension(500, 500));

        /* Create middle panel, reponsible for holding pictures of different ships */
        middlePanel = new JPanel();
        middlePanel.setBackground(Color.GRAY);
        middlePanel.setLayout(new GridLayout(3, 1)); //Setting layout for just buttons
        middlePanel.setPreferredSize(new Dimension(150, 500));


        /* Creating Initial Host and Connect buttons on JFrame */
        pushToConnect = new JButton("Connect");
        pushToHost = new JButton("Host");

        //button returns ship position
        pushToSet = new JButton("Set ships"); 
        // temp button to test ship setting
        /* Placing Buttons On the Middle Panel */
        middlePanel.add(pushToConnect);
        middlePanel.add(pushToHost);
        middlePanel.add(pushToSet);

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

    void createConnectExternalWindow() {
        String ipAddress;
        JOptionPane.showMessageDialog(frame, "Attempting to connect...", "Attempting to Connect", JOptionPane.INFORMATION_MESSAGE);
    }

    void createHostExternalWindow(String IPAddress) {
        JOptionPane.showMessageDialog(frame, "IP: " + IPAddress + "\nWaiting for connection...", "Awaiting Connection", JOptionPane.INFORMATION_MESSAGE);
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

    /* Update the middle panel with ships */
    void updateMiddlePanelPlay() {
        /* Resetting the middle panel */
        middlePanel.removeAll(); //Remove the current elements from the panel
        middlePanel.setBackground(Color.GREEN); //just for testing
        middlePanel.setLayout(new GridLayout(1, 5));
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
    public JButton getButton(int row, int column){
        return button2[row][column];
    }

    //called in controller to find clickedButton x&y position
    public int[] buttonPosition(JButton btn){
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
    public void updateView(int row, int column, String HitOrMiss){
        Font newFont = new Font("Arial", Font.BOLD, 30);
        button2[row][column].setFont(newFont);      
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

    public void updateLabel(String message){
        if(message == "Carrier"){
            carrier.setForeground(Color.RED);
        }
        if(message == "Battleship"){
            battleShip.setForeground(Color.RED);
        }
        if(message == "Cruiser"){
            cruiser.setForeground(Color.RED);
        }
        if(message == "Submarine"){
            submarine.setForeground(Color.RED);
        }
        if(message == "Destroyer"){
            destroyer.setForeground(Color.RED);
        }
    }

    public void playSoundEffect(String hitOrMiss) {
        if(hitOrMiss == "O") {
            soundEffects.playMissSound();
        }
        else { //ship has been hit or sank
            soundEffects.playHitSound();
        }
    }

    /* Search top-to-bottom through the array to find the first instance of the
     * given ship. This program will then convert the instance in the array to the
     * coordinates in the gui, which can be assigned. 
     */
    int[] findShipPosition(char[][] board, char c) {
        int x = -1, y = -1; //Will hold the positions
        //Find the first instance
        outerLoop:
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(board[i][j] == c) {
                    x = i;
                    y = j; 
                    break outerLoop;
                }
            }
        }
        x = 500 - (x*50); //Finding the position on the GUI board
        y = (y*50);
        int pos[] = {x, y};
        return pos;
    }

    /* Update the middle panel for placing ships */
    void updateMiddlePanelPlace() {
        /* Resetting the middle panel */
        middlePanel.removeAll(); //Remove the current elements from the panel
        middlePanel.setBackground(Color.GREEN); //just for testing
        middlePanel.setLayout(new GridLayout(1, 7));
        middlePanel.setPreferredSize(new Dimension(300, 100));

        /* Creating buttons to finalize the ship placement, randomizing ships, and rotating the ship */
        finalizeShipPlacement = new JButton("Finalize Placement");
        randomPlacement = new JButton("Randomize");
        
    }

    JButton getFinalizePlacementButton() {
        return finalizeShipPlacement;
    }

    JButton getRandomPlacement() {
        return randomPlacement;
    }

    JButton getrotatePlacement() {
        return rotatePlacement;
    }
}










    