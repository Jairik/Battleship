package Project1;

import java.awt.Point;
import java.io.IOException;

public class BS_Carrier{
    private int shipSize = 5;
    private boolean rotation = true;
    private String shipSymbol = "c";
    private battleshipModel model;
    
    BS_Carrier(Point coordinates, battleshipModel model){
        this.model = model;
        double x = coordinates.getX();
        double y = coordinates.getY();
        int xPos = (int)x;
        int yPos = (int)y;
        System.out.println("Carrier - x: " + xPos + " y: " + yPos);
        
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
