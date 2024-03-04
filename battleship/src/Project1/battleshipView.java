package Project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
/* 
 - Implement the View component, which includes creating two boards on JFrame. 
   Grids can be implemented using JButton (2D array of JButtons, or 2D array of labels, etc.).

 - Implement the ship placement (automatic placement, Drag-n-Drop of ships).

 - Incorporate a data model object into the View (as a data member of View using composition for example)
   and use event handler to update data model object when events happen (such as clicking a grid, placing a ship through drag-n-drop).

 - Implement the controller program to test View and Model. At this stage,
   your test program should have GUI and a Data Model backend and should update the GUI and data model accordingly.
*/
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;


public class battleshipView{
    
    private JFrame frame;
    private JPanel topPanel; //Used for the user to shoot
    private JPanel middlePanel; //Used to store ship information and stuff
    private JPanel bottomPanel; //Used for drag & drop and displaying ships
    private JButton[][] button1;
    private JButton[][] button2;
    //private ImageIcon imageIcon = new ImageIcon("/Users/will/Desktop/Cosc330/Project1/canvas1.png"); 
    private JLabel[][] label;
    SoundFX soundEffects;

    JLabel carrier;
    JLabel battleShip;
    JLabel submarine;
    JLabel destroyer;
    JLabel cruiser;

    //View constructor that builds frame, gridlayout, labels, and buttons
    battleshipView(char[][] testArr) throws IOException{

        /* Initialize sounds and start the game music */
        soundEffects = new SoundFX();
        //soundEffects.playGameMusic(); //(Uncomment this prior to submitting, its just annoying for testing)

        /* Create the root Frame */
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 600);
        frame.setLayout(new BorderLayout());       
        frame.setLocationRelativeTo(null);

        /* Create bottom panel - used to display user ships and for Drag-n-Drop */
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(10, 10));
        
        //Add grid labels for bottom of the screen
        label = new JLabel[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                String testChar = Character.toString(testArr[i][j]);
                label[i][j] = new JLabel();
                label[i][j].setText(testChar);
                label[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                bottomPanel.add(label[i][j]);
            }
        }
        bottomPanel.setPreferredSize(new Dimension(300, 300));
        
        /* Create top panel - used for shooting at enemy ships */
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(10, 10));

        //adds grid of buttons to second panel
        //button1 = new JButton[10][10];
        button2 = new JButton[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton btn = new JButton();
                button2[i][j] = btn;
                topPanel.add(btn);
            }
        }

        topPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        topPanel.setPreferredSize(new Dimension(300, 300));

        /* Create middle panel, reponsible for holding pictures of different ships */
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(1, 7)); //One for each ship and two for message or whatever (Opponents Turn, Sank ship, etc)
        middlePanel.setBackground(Color.GRAY);
        middlePanel.setLayout(new GridLayout(1, 5));
        middlePanel.setPreferredSize(new Dimension(300, 100));

        battleShip = new JLabel("Battle Ship");
        middlePanel.add(battleShip);
        carrier = new JLabel("Carrier");
        middlePanel.add(carrier);
        cruiser = new JLabel("Cruiser");
        middlePanel.add(cruiser);
        submarine = new JLabel("Sub");
        middlePanel.add(submarine);
        destroyer = new JLabel("Destroyer");
        middlePanel.add(destroyer);

        //Setting panels on the frame
        //frame.setLayout(new GridBagLayout());
        //frame = setFrameLayout(topPanel, middlePanel, bottomPanel, frame);

        frame.add(topPanel, BorderLayout.PAGE_START);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.PAGE_END);

        frame.setTitle("Battle-Ship-1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(300, 700));
        frame.pack();
        frame.setVisible(true);

        /*Display draggable Ships */
        //Get a panel to hold the different ship images
        shipPanel battleshipPanel = new shipPanel("/resources/Battleship.png"); 
        shipPanel carrierPanel = new shipPanel("/resources/Carrier.png");
        shipPanel cruiserPanel = new shipPanel("/resources/Cruiser.png");
        shipPanel submarinePanel = new shipPanel("/resources/Submarine.png");
        shipPanel destroyerPanel = new shipPanel("/resources/Destroyer.png");
        //Add all of the elements to the bottom panel
        /* 
        middlePanel.add(battleshipPanel);
        bottomPanel.add(carrierPanel);
        bottomPanel.add(cruiserPanel);
        bottomPanel.add(submarinePanel);
        bottomPanel.add(destroyerPanel);
        */
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
        button2[row][column].setText(HitOrMiss);
    }

    //testing
    public void showGameStatus(String message){
        JOptionPane.showMessageDialog(frame, message + " was sunk", "Ship Sunk!", JOptionPane.INFORMATION_MESSAGE);
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

}
    