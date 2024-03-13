package Project1;

import java.net.URL;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DraggableImage {
    ImageIcon image;
    String imagePath;
    Point imageUpperLeft, prevPoint;

    DraggableImage(String iPath, Point initialPosition) {
        imagePath = iPath;  // Store the image path
        createImageIcon();  // Initialize the ImageIcon
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

    //Creates an image icon given a file path. Serves as a helper function the the constructor
    private void createImageIcon() {
        try {
            URL shipImagePath = DraggableImage.class.getResource(imagePath);
            InputStream inputStream = shipImagePath.openStream();
            Image image = ImageIO.read(inputStream);
            inputStream.close();
            this.image = new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getXCoordinate() {
        return (int) (imageUpperLeft.getX() / 50);
    }
    
    public int getYCoordinate() {
        return (int) (imageUpperLeft.getY() / 50);
    }
    public String getImagePath() {
        return imagePath;
    }
    public Point getImageUpperLeft() {
        return imageUpperLeft;
    }
    

}