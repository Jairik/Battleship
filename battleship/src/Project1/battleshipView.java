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

public class battleshipView implements ActionListener{
    
    private JFrame frame;
    private JPanel panel1;
    private JPanel panel2;
    private JButton[][] button1;
    private JButton[][] button2;
    private ImageIcon imageIcon = new ImageIcon("/Users/will/Desktop/Cosc330/Project1/canvas1.png");

    battleshipView() throws IOException{

        //board1
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        //frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        //frame.setLayout(new GridLayout(2, 1));

        MyPanel myPanel = new MyPanel(imageIcon);
        myPanel.setLayout(new GridLayout(10, 10));
        
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(10, 10));

        button1 = new JButton[10][10];
        button2 = new JButton[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                //JButton btn = new JButton();
                JLabel label = new JLabel();
                JButton btn2 = new JButton();
                //button1[i][j] = btn;
                button2[i][j] = btn2;
                //btn.setBackground(Color.BLUE);
                btn2.setBackground(Color.BLUE);
                myPanel.add(label);
                panel2.add(btn2);
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

    public void fireCannon(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                JButton btn = button2[i][j];
                btn.addActionListener(this);
            }
        }
    }

    public JButton getButton(int row, int column){
        return button2[row][column];
    }

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

    public void updateView(int row, int column){
        button2[row][column].setText("X");
    }
    
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
//drag n drop

class MyPanel extends JPanel{
    ImageIcon image;
    Point imageUpperLeft, prevPoint;
    MyPanel(ImageIcon imageIcon){
        image = imageIcon;
        imageUpperLeft = new Point(100,100);
        prevPoint = imageUpperLeft;
        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);
        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.paintIcon(this, g, (int) imageUpperLeft.getX(), (int)
        imageUpperLeft.getY());
    }
    private class ClickListener extends MouseAdapter{
        public void mousePressed(MouseEvent event) {
            prevPoint = event.getPoint();
        }   
    }
    private class DragListener extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent event) {
            Point currPoint = event.getPoint();
            int dx = (int) (currPoint.getX() - prevPoint.getX());
            int dy = (int) (currPoint.getY() - prevPoint.getY());

            imageUpperLeft.translate(dx, dy);
            prevPoint = currPoint;
            repaint();
            }
        }
    }
    
/* 
    class MyFrame extends JFrame {
        MyFrame(ImageIcon imageIcon){
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(800,800);
            this.setLocationRelativeTo(null);
            MyPanel myPanel = new MyPanel(imageIcon);
            myPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            myPanel.setSize(imageIcon.getIconHeight(), imageIcon.getIconWidth());
            this.add(myPanel);
            this.setSize(imageIcon.getIconHeight()*2, imageIcon.getIconWidth()*2);
            this.setBackground(Color.CYAN);
            this.setVisible(true);
        }
    }
*/