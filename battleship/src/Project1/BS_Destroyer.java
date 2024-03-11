package Project1;

import java.awt.Point;

public class BS_Destroyer {
    private int shipSize = 2;
    private boolean rotation = false;
    private String shipSymbol = "d";

    BS_Destroyer(Point coordinates){
        double x = coordinates.getX();
        double y = coordinates.getY();
        int xPos = (int)x;
        int yPos = (int)y;

        //updates ships x coordinate if no rotation
        if(rotation = false){
            while(shipSize > 0){
                //update ship model.setShip(xPos, yPos, shipSymbol);
                xPos++;
                shipSize--;
            }
        }
    }
}
