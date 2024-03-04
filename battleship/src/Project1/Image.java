package Project1;

/*
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/*/

/*public class ImagePanel extends JPanel{
    
    ImageIcon image = new ImageIcon("canvas1.png");
    final int IMG_WIDTH = image.getIconWidth();
    final int IMG_HEIGHT = image.getIconHeight();

    Point image_corner;
    Point previousPoint;
    
    ImagePanel(){
        image_corner = new Point(0, 0);

        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);

        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        image.paintIcon(this, g, (int)image_corner.getX(), (int)image_corner.getY());

    }

    private class ClickListener extends MouseAdapter {

        public void mousePressed(MouseEvent evt){
            previousPoint = evt.getPoint();
        }
    }

    private class DragListener extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent evt){
            Point currentPoint = evt.getPoint();

            image_corner.translate(
                (int)(currentPoint.getX() - previousPoint.getX()) ,
                (int)(currentPoint.getY() - previousPoint.getY())
                );
            previousPoint = currentPoint;
            repaint();
        }
    }
    /*
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            /* 
            JFrame frame = new JFrame("Image Panel Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            ImagePanel imagePanel = new ImagePanel();
            frame.add(imagePanel);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
        });
    }
}


import javax.imageio.ImageIO;
import javax.swing.*;

}*/
/* 
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL; 

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
            this.getContentPane().setPreferredSize(new Dimension(300, 600));
            this.setVisible(true);
        }
    }
public class Image {
    public static void main(String[] args) throws IOException {
        /*File file = new File("/resources/canvas1.png");
        BufferedImage bufferedImage = ImageIO.read(file);
        ImageIcon imageIcon = createImageIcon("/resources/canvas1.png");
        JFrame jFrame = new MyFrame(imageIcon);
    }

    public static ImageIcon createImageIcon(String imagePath) {
        ImageIcon shipIcon = null; //Initializing with null value
        try {
            URL shipImagePath = shipPanel.class.getResource(imagePath);
            System.out.println(imagePath);
            InputStream inputStream = shipImagePath.openStream();
            BufferedImage image = ImageIO.read(inputStream);
            inputStream.close();
            shipIcon = new ImageIcon(image);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return shipIcon;
    }
} 

/*
        File file = new File("/Users/will/Desktop/Cosc330/Project1/canvas1.png");
        BufferedImage bufferedImage = ImageIO.read(file);
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        JFrame jFrame = new MyFrame(imageIcon);
    }
} 
*/