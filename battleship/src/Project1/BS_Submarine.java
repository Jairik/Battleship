package Project1;

import java.awt.Point;

public class BS_Submarine {
    private int shipSize = 3;
    private boolean rotation = false;
    private String shipSymbol = "s";
    private battleshipModel model;

    BS_Submarine(Point coordinates){
        double x = coordinates.getX();
        double y = coordinates.getY();
        int xPos = (int)x;
        int yPos = (int)y;

        //updates ships x coordinate if no rotation
        if(rotation = false){
            while(shipSize > 0){
                model.setModel(xPos, yPos, shipSymbol);
                xPos++;
                shipSize--;
            }
        }

    }
}
