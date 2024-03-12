package Project1;

import java.awt.Point;

public class BS_Cruiser {
    private int shipSize = 3;
    private boolean rotation = true;
    private String shipSymbol = "r";
    private battleshipModel model;

    BS_Cruiser(Point coordinates, battleshipModel model){
        this.model = model;
        double x = coordinates.getX();
        double y = coordinates.getY();
        int xPos = (int)x;
        int yPos = (int)y;
        System.out.println("Cruiser - x: " + xPos + " y: " + yPos);

        //updates ships x coordinate if no rotation
        if(rotation){
            while(shipSize > 0){
                model.setModel(xPos, yPos, shipSymbol);
                xPos++;
                shipSize--;
            }
        }

    }
}
