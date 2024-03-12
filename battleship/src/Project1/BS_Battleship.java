package Project1;

import java.awt.Point;

public class BS_Battleship {
    private int shipSize = 4;
    private boolean rotation = true;
    private String shipSymbol = "b";
    private battleshipModel model;
    

    BS_Battleship(Point coordinates, battleshipModel model){
        this.model = model;
        double x = coordinates.getX();
        double y = coordinates.getY();
        int xPos = (int)x;
        int yPos = (int)y;

        //updates ships x coordinate if no rotation
        if(rotation){
            while(shipSize > 0){
                model.setModel(xPos, yPos, shipSymbol);
                yPos++;
                shipSize--;
            }
        }
    }
}
