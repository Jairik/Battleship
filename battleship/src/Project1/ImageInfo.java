package Project1;

import java.awt.Point;

public class ImageInfo {
    private Point coordinates;
    private String imagePath;

    public ImageInfo(Point coordinates, String imagePath) {
        this.coordinates = coordinates;
        this.imagePath = imagePath;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public String getImagePath() {
        return imagePath;
    }
}
