package Project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class battleshipView{
    
    private JFrame frame;
    private JPanel panel1;
    private JPanel panel2;
    private JButton[][] button1;
    private JButton[][] button2;
    //private ImageIcon imageIcon = new ImageIcon("/Users/will/Desktop/Cosc330/Project1/canvas1.png"); 
    private JLabel[][] label;

    //View constructor that builds frame
    battleshipView() throws IOException{

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());       
        frame.setLocationRelativeTo(null);

        shipPanel myPanel = new shipPanel("/resources/canvas1.png"); //Panel to hold ship image
        myPanel.setLayout(new GridLayout(10, 10));

        label = new JLabel[10][10];
        
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JLabel lbl = new JLabel();
                label[i][j] = lbl;
                lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                myPanel.add(lbl);
            }
        }
        
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(10, 10));

        button1 = new JButton[10][10];
        button2 = new JButton[10][10];

        
        
        //adds grid of buttons to second panel
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton btn = new JButton();
                button2[i][j] = btn;
                panel2.add(btn);
            }
        }

        myPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        frame.add(myPanel);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.setTitle("Battle-Ship-1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(300, 600));
        frame.pack();
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
        button2[row][column].setText(HitOrMiss);
    }

    public void showGameStatus(){
        String message = "ship c sunk";
        JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
    