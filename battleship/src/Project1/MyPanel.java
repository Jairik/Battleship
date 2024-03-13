package Project1;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
    List<DraggableImage> images = new ArrayList<>();

    //constructor iterates over imaagePath list and creates draggableImage instance for each
    MyPanel(List<String> imagePaths) {
        setLayout(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout

        int initialX = 100;//positions[0]; // Initial x-coordinate
        int initialY = 100;//positions[1]; // Initial y-coordinate

        for (String imagePath : imagePaths) {
            //ImageIcon imageIcon;
            DraggableImage draggableImage = new DraggableImage(imagePath, new Point(initialX, initialY));              
            images.add(draggableImage);
            initialY += 50;// Adjust the gap between images
        }
        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);
        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);
    }

    //paints images onto panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (DraggableImage image : images) {
            image.paintIcon(this, g);
        }
    }

    //returns image path and coordinates
    public List<ImageInfo> getImagesInfo() {
        List<ImageInfo> imagesInfo = new ArrayList<>();
        for (DraggableImage image : images) {
            int xCoordinate = image.getXCoordinate();
            int yCoordinate = image.getYCoordinate();
            String imagePath = image.getImagePath();
            imagesInfo.add(new ImageInfo(new Point(xCoordinate, yCoordinate), imagePath));
        }
        return imagesInfo;
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
    public List<DraggableImage> getImages() {
        return images;
    }

    //replaces an imagepath in list for another. Used to toggle vertical and horizontal ships
    public void replaceImage(String oldImagePath, String newImagePath) {
        for (int i = 0; i < images.size(); i++) {
            DraggableImage image = images.get(i);
            if (image.getImagePath().equals(oldImagePath)) {
                // Replace the old image with a new one
                images.set(i, new DraggableImage(newImagePath, image.getImageUpperLeft()));
                repaint(); // Repaint the panel to reflect the changes
                return; // Exit the loop after the replacement
            }
        }
    }

    //after set ships clicked. makes images non-movable
    public void setImagesMovable(boolean moveable) {
        for (DraggableImage image : images) {
            image.setMovable(moveable);
        }
    }

}