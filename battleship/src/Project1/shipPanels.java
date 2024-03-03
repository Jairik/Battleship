package Project1;

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

/* -- Holds all the code for the draggable ships -- 
 * - We can easily split this into seperate files if need be
 * - All ships should follow the same logic, however making them 'snappable' may be weird
 * so I am not going to implement that yet
*/

/*To open/run this, we would need to run the following things in a controller function (controller since we must access model)
 *      File file = new File("<filePath>");
        BufferedImage bufferedImage = ImageIO.read(file);
        ImageIcon <shipName> = new ImageIcon(bufferedImage);
        JFrame <shipName>Frame = new MyFrame(<shipName>);
 */

//Creating a draggable panel for Carrier Ship
class carrierPanel extends JPanel{
    final int gridSize = 10;
    ImageIcon carrierImage;
    Point upperLeftCoordinate, prevGridCoordinate;
    carrierPanel(ImageIcon carrierIcon){
        carrierImage = carrierIcon;
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
        carrierImage.paintIcon(this, g, (int) upperLeftCoordinate.getX() * gridSize, (int)
        upperLeftCoordinate.getY() * gridSize);
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
