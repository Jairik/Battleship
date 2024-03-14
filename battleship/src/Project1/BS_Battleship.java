package Project1;

import java.awt.Point;

public class BS_Battleship {
    private int shipSize = 4;
    private char shipSymbol = 'b';
    @SuppressWarnings("unused")
    private battleshipModel model;

    BS_Battleship(Point coordinates, battleshipModel model, boolean rotation){
        this.model = model;
        double x = coordinates.getX();
        double y = coordinates.getY();
        int xPos = (int)x;
        int yPos = (int)y;
        System.out.println("battleship - x: " + xPos + " y: " + yPos);

        //updates ships x coordinate if no rotation
        if(!rotation){
            while(shipSize > 0){
                model.setModel(xPos, yPos, shipSymbol);
                xPos++;
                shipSize--;
            }
        }
        else{
            while(shipSize > 0){
                model.setModel(xPos, yPos, shipSymbol);
                yPos++;
                shipSize--;
            }
        }
    }

    /* Looks at the model and resets the positions */
    void resetPosition() {
        char[][] board = model.getUserBoard();
        int x = 0, y = 0; //Holds the positions of the ships
        outerloop:
        //Find the first instance of the character in the model
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(board[i][j] == shipSymbol) {
                    x = i;
                    y = j;
                    break outerloop;
                }
            }
        }
        //Convert point in the array to a point on the screen
        x = 500 - (x*50);
        y = (y*50);

        //Create a new point
        Point newPoint = new Point(x, y);

        //Place the image at this point
        
    }
}
