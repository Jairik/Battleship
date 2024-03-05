package Project1;

    import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DragandDropExample {
    private JFrame frame;
    private JPanel gridPanel;
    private List<DraggableImage> images;

    private int gridSize = 50; // Size of the grid

    public DragandDropExample() {
        frame = new JFrame("Drag and Drop Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        gridPanel = new JPanel();
        gridPanel.setLayout(null);
        gridPanel.setBackground(Color.WHITE);

        images = new ArrayList<>();
        createDraggableImage(new ImageIcon("path/to/image1.png"), 50, 50);
        createDraggableImage(new ImageIcon("path/to/image2.png"), 150, 150);

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private void createDraggableImage(ImageIcon imageIcon, int x, int y) {
        DraggableImage draggableImage = new DraggableImage(imageIcon);
        draggableImage.setBounds(x, y, draggableImage.getWidth(), draggableImage.getHeight());

        draggableImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                draggableImage.onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggableImage.onMouseReleased(e);
            }
        });

        draggableImage.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draggableImage.onMouseDragged(e);
            }
        });

        gridPanel.add(draggableImage);
        images.add(draggableImage);
    }

    private class DraggableImage extends JLabel {
        private Point startDrag;
        private Point startLocation;

        DraggableImage(ImageIcon icon) {
            super(icon);
        }

        void onMousePressed(MouseEvent e) {
            startDrag = new Point(e.getX(), e.getY());
            startLocation = getLocation();
        }

        void onMouseReleased(MouseEvent e) {
            snapToGrid();
        }

        void onMouseDragged(MouseEvent e) {
            int deltaX = e.getX() - startDrag.x;
            int deltaY = e.getY() - startDrag.y;

            setLocation(startLocation.x + deltaX, startLocation.y + deltaY);
        }

        void snapToGrid() {
            int snappedX = getSnappedCoordinate(getX(), gridSize);
            int snappedY = getSnappedCoordinate(getY(), gridSize);

            setLocation(snappedX, snappedY);
        }

        private int getSnappedCoordinate(int coordinate, int gridSize) {
            return Math.round((float) coordinate / gridSize) * gridSize;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DragandDropExample::new);
    }
}

