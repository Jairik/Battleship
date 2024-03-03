package Project1;

/*Authors: JJ McCauley & Will Lamuth
Creation Date: 2/24/24 
Last Update: 3/3/24 */

import javax.swing.*;
import java.awt.*;

public class battleshipModel {
    //Different Ships (Can delete later, just helpful for implementing board)
    final char battleShip = 'b'; 
    final int battleShipSize = 4; 
    final char carrier = 'c'; 
    final int carrierSize = 5;
    final char cruiser = 'r';
    final int cruiserSize = 3;
    final char submarine = 's';
    final int submarineSize = 3;
    final char destroyer = 'd'; 
    final int destroyerSize = 2;

    final int boardHeight = 10;
    final int boardWidth = 10;

    char[][] board; //2d board to hold the different ships for the user
    char[][] opponentBoard; //2d board to hold the placements, hits, and misses on opponents board
    int moveCounter; //count the number of moves
    boolean turn; //Boolean may not be the best dataType
    //Ships 
    int carrierRemaining = 5;
    int battleshipRemaining = 4;
    int cruiserRemaining = 4;
    int submarineRemaining = 4;
    int destroyerRemaining = 4;

    //Constructor to make board and declare currentTurn
    public battleshipModel() {
        board = new char[boardHeight][boardWidth];
        opponentBoard = new char[boardHeight][boardWidth];
        randomlySetBoard();
        //randomlySetBoard();
        //initBoard();
    }
    // will - called in controller to check valid shot
    /*Validates a user's shot, returning true if the shot will fall on an empty place on the array*/
    public boolean checkForValidShot(int x, int y) {
        return(board[x][y] == ' ');
    }

    //will - initializes a board with ' ' then i hardcoded a ship into it with 'c'. Doing this for testing
    public void initBoard() {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                board[i][j] = ' ';
            }
        }
        //fake ship
        board[4][0] = 'c';
        board[4][1] = 'c';
        board[4][2] = 'c';
    }

    /* will - after the button clicked in controller it updates the model accordingly
    public void updateModel(int x, int y, String HitOrMiss){
        board[x][y] = HitOrMiss.charAt(0); // changes string to char
    } */

    /*will - function to recognize ship sank
    public boolean shipStatus(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(board[i][j] == 'c'){
                    return false;
                }
            }
        }
        return true;
    }*/

    //Logic to randomly place the ships on the board
    void randomlySetBoard() {
        clearBoard();
        randomlyPlaceShip(carrierSize, carrier);
        randomlyPlaceShip(battleShipSize, battleShip);
        randomlyPlaceShip(cruiserSize, cruiser);
        randomlyPlaceShip(submarineSize, submarine);
        randomlyPlaceShip(destroyerSize, destroyer);
    }

     //Helper function for randomly setting the board
    void clearBoard() {
        for(int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = ' ';
            }
        }
    }

    //Given a shipSize, will find a section of the board that is not taken and place the given ship there
    //Notes: Definitely not the most efficient way to do it but it works for now
    void randomlyPlaceShip(int shipSize, char currentShip) {
        boolean placeAgain = true;
        while(placeAgain) {
            int ranX = (int)(Math.random() * (boardWidth)); //Getting a random X coordinate
            int ranY = (int)(Math.random() * (boardHeight)); //Getting a random Y coordinate
            int dir = (int)(Math.random() * 2); //Getting a random int to represent direction
            int iterator = 0; //Used to iterate across the board to check if spots are valid or taken
            int shipPlacedCount = 0;
            if (dir == 0) { //Move right across the board
                //Check each spot to see if the spot is empty or out of bounds
                while(iterator < shipSize) {
                    if((ranX+iterator) < boardWidth && board[ranX+iterator][ranY] == ' ') {
                        shipPlacedCount++;
                    }
                    iterator++;
                }             
            }
            else { //Move down the board
                //Check each spot to see if the spot is empty or out of bounds
                while(iterator < shipSize) {
                    if((ranY+iterator) < boardHeight && board[ranX][ranY+iterator] == ' ') {
                        shipPlacedCount++;
                    }
                    iterator++;
                }
            }
            if(shipPlacedCount == shipSize) {
                placeAgain = false;
                for(int i = 0; i < shipSize; i++) {
                    if (dir == 0) {
                        board[ranX+i][ranY] = currentShip;
                    } 
                    else {
                        board[ranX][ranY+i] = currentShip;
                    }
                }
            }
        }
    }


    /*Returns a String corresponding to whether the shot was a hit, miss, or sink.
    Hit:  X
    Miss: O
    Sink: <shipName>
    */
    /*IMPORTANT NOTE: We are going to later have to set this to opponentBoard, however board
     * works now for testing
     */
    String determineHit(int xPos, int yPos) {
        String hit;
        boolean shipSank = checkForSinkShip(xPos, yPos);
        if(board[xPos][yPos] != ' ') {
            board[xPos][yPos] = 'O';
            hit = "O";
        }
        else {
            if(board[xPos][yPos] == carrier) {
                board[xPos][yPos] = 'X';
                hit = "X";
                carrierRemaining--;
                if(shipSank) {
                    hit = "Carrier";
                }
            }
            else if(board[xPos][yPos] == battleShip) {
                board[xPos][yPos] = 'X';
                hit = "X";
                battleshipRemaining--;
                if(shipSank) {
                    hit = "Battleship";
                }
            }
            else if(board[xPos][yPos] == cruiser) {
                board[xPos][yPos] = 'X';
                hit = "X";
                cruiserRemaining--;
                if(shipSank) {
                    hit = "Cruiser";
                }
            }
            else if(board[xPos][yPos] == submarine) {
                board[xPos][yPos] = 'X';
                hit = "X";
                submarineRemaining--;
                if(shipSank) {
                    hit = "Submarine";
                }
            }
            else{
                board[xPos][yPos] = 'X';
                hit = "X";
                destroyerRemaining--;
                if(shipSank) {
                    hit = "Destroyer";
                }
            }
        }
        return hit;
    }

    //Not entirely sure how I am going to implement this yet
    //NOTE: WE WILL NEED TO CHANGE THIS TO OPPONENTBOARD
    boolean checkForSinkShip(int xPos, int yPos) {
        boolean sank = false;
        int numOfShips = 5;
        char shipArr[] = {carrier, battleShip, cruiser, submarine, destroyer};
        int shipHitsRemainingArr[] = {carrierRemaining, battleshipRemaining, cruiserRemaining, submarineRemaining, destroyerRemaining};
        for(int i = 0; i < numOfShips; i++) {
            if(board[xPos][yPos] == shipArr[i] && shipHitsRemainingArr[i] == 1) {
                sank = true;
                break; //skip the rest of the iterations
            }
        }
        return sank;
    }

    //Returns the board back to the controller
    char[][] getBoard() {
        return board;
    }

}
