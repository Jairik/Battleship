package Project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
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
import java.util.List;

//drag n drop imports
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class battleshipView{
    
    private JFrame frame;
    private JPanel topPanel; //Used for the user to shoot
    private JPanel middlePanel; //Used to store ship information and stuff
    private MyPanel bottomPanel; //Used for drag & drop and displaying ships
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

        //list of images to pass into MyPanel bottomPanel
        List<String> imagePaths = new ArrayList<>();
        imagePaths.add("/Users/will/Desktop/COSC330-BattleShip/battleship/src/resources/Battleship.png");
        imagePaths.add("/Users/will/Desktop/COSC330-BattleShip/battleship/src/resources/Carrier.png");
        imagePaths.add("/Users/will/Desktop/COSC330-BattleShip/battleship/src/resources/Cruiser.png");
        imagePaths.add("/Users/will/Desktop/COSC330-BattleShip/battleship/src/resources/Submarine.png");
        imagePaths.add("/Users/will/Desktop/COSC330-BattleShip/battleship/src/resources/Destroyer.png");
        /* Initialize sounds and start the game music */
        soundEffects = new SoundFX();

        /* Create the root Frame */
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 600);
        frame.setLayout(new BorderLayout());       
        frame.setLocationRelativeTo(null);

        /* Create bottom panel - used to display user ships and for Drag-n-Drop */
        bottomPanel = new MyPanel(imagePaths);
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
        //middlePanel.setLayout(new GridLayout(1, 7)); //One for each ship and two for message or whatever (Opponents Turn, Sank ship, etc)
        middlePanel.setBackground(Color.GRAY);
        middlePanel.setLayout(new GridLayout(1, 5));
        middlePanel.setPreferredSize(new Dimension(300, 100));

        /* 
        Font newFont = label.getFont().deriveFont(14.0f);
        battleShip.setFont(newFont);
        */
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

        frame.add(topPanel, BorderLayout.PAGE_START);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.PAGE_END);

        frame.setTitle("Battle-Ship-1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(300, 700));
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
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

}

//drag and drop classes
class MyPanel extends JPanel {
    List<DraggableImage> images = new ArrayList<>();

    MyPanel(List<String> imagePaths) {
        setLayout(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout

        int initialX = 100; // Initial x-coordinate
        int initialY = 20; // Initial y-coordinate

        for (String imagePath : imagePaths) {
            try {
                BufferedImage imageIcon = ImageIO.read(new File(imagePath));
                System.out.println("Image loaded successfully: " + imagePath);
                DraggableImage draggableImage = new DraggableImage(imageIcon, new Point(initialX, initialY));
                images.add(draggableImage);
                initialY += 30;// Adjust the gap between images
            } catch (IOException e) {
                System.out.println("Image not loaded successfully: ");
                e.printStackTrace();
            }
        }

        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);
        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (DraggableImage image : images) {
            image.paintIcon(this, g);
        }
    }

    private class ClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent event) {
            for (DraggableImage image : images) {
                image.mousePressed(event.getPoint());
            }
        }
    }

    private class DragListener extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent event) {
            for (DraggableImage image : images) {
                if (image.contains(event.getPoint())) {
                    image.mouseDragged(event.getPoint());
                }
            }
            repaint();
        }
    }
}

class DraggableImage {
    ImageIcon image;
    Point imageUpperLeft, prevPoint;

    DraggableImage(BufferedImage imageIcon, Point initialPosition) {
        image = new ImageIcon(imageIcon);
        imageUpperLeft = initialPosition;
        prevPoint = imageUpperLeft;
    }

    public void paintIcon(Component c, Graphics g) {
        image.paintIcon(c, g, (int) imageUpperLeft.getX(), (int) imageUpperLeft.getY());
    }

    public void mousePressed(Point point) {
        prevPoint = point;
    }

    public void mouseDragged(Point currPoint) {
        int dx = (int) (currPoint.getX() - prevPoint.getX());
        int dy = (int) (currPoint.getY() - prevPoint.getY());
        imageUpperLeft.translate(dx, dy);
        prevPoint = currPoint;
    }

    public boolean contains(Point point) {
        int x = (int) imageUpperLeft.getX();
        int y = (int) imageUpperLeft.getY();
        int width = image.getIconWidth();
        int height = image.getIconHeight();

        return (point.getX() >= x && point.getX() <= x + width &&
                point.getY() >= y && point.getY() <= y + height);
    }
}
    