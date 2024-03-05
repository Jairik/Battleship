package Project1;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/* -- Holds all the code for the draggable ships -- 
 * - We can easily split this into seperate files if need be
 * - All ships should follow the same logic, however making them 'snappable' may be weird
 * so I am not going to implement that yet
*/

/*To open/run this, we would need to run the following things in a controller function (controller since we must access model)
 *      JFrame <shipName>Frame = new MyFrame("/resources/<file_name>");
 *
 */

//Creating a draggable panel for Carrier Ship


class shipPanel extends JPanel{
    final int gridSize = 10;
    ImageIcon shipImage;
    Point upperLeftCoordinate, prevGridCoordinate;
    //Constructor
    shipPanel(String imagePath) {
        ImageIcon shipIcon = createImageIcon(imagePath);
        shipImage = shipIcon;
        upperLeftCoordinate = new Point(100,100);
        //Get the points to pass into new Point()
        prevGridCoordinate = new Point();
        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);
        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        shipImage.paintIcon(this, g, (int) upperLeftCoordinate.getX() * gridSize, (int)
        upperLeftCoordinate.getY() * gridSize);
    }

    //Creates an image icon given a file path. Serves as a helper function the the constructor
    public ImageIcon createImageIcon(String imagePath) {
        ImageIcon shipIcon = null; //Initializing with null value
        try {
            System.out.println(imagePath);
            URL shipImagePath = shipPanel.class.getResource(imagePath);
            InputStream inputStream = shipImagePath.openStream();
            Image image = ImageIO.read(inputStream);
            inputStream.close();
            shipIcon = new ImageIcon(image);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return shipIcon;
    }
    private class ClickListener extends MouseAdapter{
        public void mousePressed(MouseEvent event) {
            prevGridCoordinate = event.getPoint();
        }   
    }
    private class DragListener extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent event) {
            Point currPoint = event.getPoint();
            Point currentGridCoordinate = getGridCoordinates(currPoint);

            int dx = (int) (currentGridCoordinate.getX() - prevGridCoordinate.getX());
            int dy = (int) (currentGridCoordinate.getY() - prevGridCoordinate.getY());

            upperLeftCoordinate.translate(dx, dy);
            prevGridCoordinate = currPoint;

            repaint();

            }
        }

        private Point getGridCoordinates(Point currentPixelCoordinate) {
            int xPos = (int) (currentPixelCoordinate.getX() / gridSize);
            int yPos = (int) (currentPixelCoordinate.getY() / gridSize);

            return new Point(xPos, yPos);
        }
    }

/*
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

class MyPanel extends JPanel {
    List<DraggableImage> images = new ArrayList<>();

    MyPanel(List<String> imagePaths) {
        setLayout(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout

        int initialX = 100; // Initial x-coordinate
        int initialY = 100; // Initial y-coordinate

        for (String imagePath : imagePaths) {
            try {
                BufferedImage imageIcon = ImageIO.read(new File(imagePath));
                DraggableImage draggableImage = new DraggableImage(imageIcon, new Point(initialX, initialY));
                images.add(draggableImage);
                initialY += 100;// Adjust the gap between images
            } catch (IOException e) {
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

public class shipPanel {
    public static void main(String[] args) {
        List<String> imagePaths = new ArrayList<>();
        imagePaths.add("canvas1.png");
        imagePaths.add("Submarine.png");
        imagePaths.add("Battleship.png");
        imagePaths.add("Cruiser.png");
        imagePaths.add("Destroyer.png");

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(800, 800);
        jFrame.setLocationRelativeTo(null);

        MyPanel myPanel = new MyPanel(imagePaths);
        myPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        jFrame.add(myPanel);
        jFrame.setBackground(Color.CYAN);
        jFrame.setVisible(true);
    }
}
*/