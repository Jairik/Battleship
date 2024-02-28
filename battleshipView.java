package Project1;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
/* 
 - Implement the View component, which includes creating two boards on JFrame. 
   Grids can be implemented using JButton (2D array of JButtons, or 2D array of labels, etc.).

 - Implement the ship placement (automatic placement, Drag-n-Drop of ships).

 - Incorporate a data model object into the View (as a data member of View using composition for example)
   and use event handler to update data model object when events happen (such as clicking a grid, placing a ship through drag-n-drop).

 - Implement the controller program to test View and Model. At this stage,
   your test program should have GUI and a Data Model backend and should update the GUI and data model accordingly.
*/

public class battleshipView implements ActionListener{
    
    private JFrame frame;
    private JPanel panel1;
    private JPanel panel2;
    private JButton[][] button1;
    private JButton[][] button2;

    //constructor that builds the UI
    public battleshipView(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        //frame.setLayout(new GridLayout(2, 1));

        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(10, 10));
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(10, 10));

        button1 = new JButton[10][10];
        button2 = new JButton[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton btn = new JButton();
                JButton btn2 = new JButton();
                button1[i][j] = btn;
                button2[i][j] = btn2;
                btn.setBackground(Color.BLUE);
                btn2.setBackground(Color.BLUE);
                panel1.add(btn);
                panel2.add(btn2);
            }
        }

        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.setTitle("Battle-Ship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(300, 600));
        frame.pack();
        frame.setVisible(true);
    }

    //method handles firing placement
    public void fireCannon(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton btn = button1[i][j];
                //action listener gives button function
                btn.addActionListener(this);
            }
        }
    }

    //method returns JButton
    public JButton getButton(int row, int column){
        return button1[row][column];
    }

    //finds clicked button position
    public int[] buttonPosition(JButton btn){
        // Find the button's position in the array
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (button1[i][j] == btn) {
                   return new int[]{i, j};
                }
            }
        }
        return null;
    }

    //places text to corresponding clicked button
    public void updateView(int row, int column){
        button1[row][column].setText("X");
    }
        
    //button function
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton)e.getSource();
        //finds clicked button position
        int[] position = buttonPosition(clickedButton);

        updateView(position[0], position[1]);
    }

    public JLabel getPictuerLabel(String pictureFilePath) throws IOException {

        File file = new File(pictureFilePath);
        BufferedImage bufferedImage = ImageIO.read(file);

        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        JLabel battleShip = new JLabel(imageIcon);
        return battleShip;
    }
}
/*
class MyPanel extends JPanel{
        ImageIcon image;
        Point imageUpperLeft, prevPoint;

        MyPanel(ImageIcon imageIcon){
            image = imageIcon;
            
        }
    }
    */