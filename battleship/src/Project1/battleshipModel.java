package Project1;

/*Authors: JJ McCauley & Will Lamuth
Creation Date: 2/24/24 
Last Update: 2/24/24 */

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

    //Constructor to make board and declare currentTurn
    public battleshipModel() {
        board = new char[boardHeight][boardWidth];
        opponentBoard = new char[boardHeight][boardWidth];
        randomlySetBoard();
        while(/*Some button is not pushed or something*/ true) {
            userPlaceShip();
        }
    }

    //Potentially Not needed
    void clearBoard() {
        for(int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = ' ';
            }
        }
    }

    //Logic to randomly place the ships on the board
    void randomlySetBoard() {
        clearBoard();
        randomlyPlaceShip(carrierSize, carrier);
        randomlyPlaceShip(battleShipSize, battleShip);
        randomlyPlaceShip(cruiserSize, cruiser);
        randomlyPlaceShip(submarineSize, submarine);
        randomlyPlaceShip(destroyerSize, destroyer);
    }

    //Given a shipSize, will find a section of the board that is not taken and place the given ship there
    //Notes: Definitely not the most efficient way to do it but it works for now
    void randomlyPlaceShip(int shipSize, char currentShip) {
        int ranX = (int)(Math.random()) * (boardWidth+1); //Getting a random X coordinate
        int ranY = (int)(Math.random()) * (boardHeight+1); //Getting a random Y coordinate
        int dir = (int)(Math.random()) * (4); //Getting a random int to represent direction
        int iterator = 0; //Used to iterate across the board to check if spots are valid or taken
        if (dir == 0) { //Move right across the board
            //Check each spot to see if the spot is empty or out of bounds
            while(iterator < shipSize) {
                //If it is off the board or is nonempty
                if(ranX > (boardWidth+1) || ranY > (boardHeight+1) || board[ranX+iterator][ranY] != ' ') {
                    randomlyPlaceShip(shipSize, currentShip); //Recursively call itself to try again
                }
                iterator++;
            }
            //Since all spots are valid, place the ships
            iterator = 0;
            while(iterator < shipSize) {
                board[ranX+iterator][ranY] = currentShip;
                iterator++;
            }
        }
        else if (dir == 1) { //Move left across the board
            //Check each spot to see if the spot is empty or out of bounds
            while(iterator < shipSize) {
                //If it is off the board or is nonempty
                if(ranX > (boardWidth+1) || ranY > (boardHeight+1) || board[ranX-iterator][ranY] != ' ') {
                    randomlyPlaceShip(shipSize, currentShip); //Recursively call itself to try again
                }
                iterator++;
            }
            //Since all spots are valid, place the ships
            iterator = 0;
            while(iterator < shipSize) {
                board[ranX-iterator][ranY] = currentShip;
                iterator++;
            }
        }
        else if (dir == 2) { //Move up the board
            //Check each spot to see if the spot is empty or out of bounds
            while(iterator < shipSize) {
                //If it is off the board or is nonempty
                if(ranX > (boardWidth+1) || ranY > (boardHeight+1) || board[ranX][ranY+iterator] != ' ') {
                    randomlyPlaceShip(shipSize, currentShip); //Recursively call itself to try again
                }
                iterator++;
            }
            //Since all spots are valid, place the ships
            iterator = 0;
            while(iterator < shipSize) {
                board[ranX][ranY+iterator] = currentShip;
                iterator++;
            }
        }
        else { //Move down the board
            //Check each spot to see if the spot is empty or out of bounds
            while(iterator < shipSize) {
                //If it is off the board or is nonempty
                if(ranX > (boardWidth+1) || ranY > (boardHeight+1) || board[ranX][ranY-iterator] != ' ') {
                    randomlyPlaceShip(shipSize, currentShip); //Recursively call itself to try again
                }
                iterator++;
            }
            //Since all spots are valid, place the ships
            iterator = 0;
            while(iterator < shipSize) {
                board[ranX][ranY-iterator] = currentShip;
                iterator++;
            }
        }
    }

    //Returns true if the user's movement is valid
    boolean userPlaceShip(/*something here */) {
        /////
        return true;
        //////
    }

    //Will check if the shot by the user is valid
    boolean checkForValidShot(int xPos, int yPos) {
        boolean validShot = false;
        if(board[xPos][yPos] != 'X' || board[xPos][yPos] != 'O') {
            validShot = true;
        }
        return validShot;
    }

    /*This method will check with the shot at the given coordinates are hits or misses. If it is a miss, 
     * 'O' will be returned. If it is a hit, the String of the corresponding ship will be returned. */
    String getHitMessage(int xPos, int yPos) {
        String returnMessage; 
        String hitOrSink = "hit";
        if(opponentBoard[xPos][yPos] != ' ') {
            opponentBoard[xPos][yPos] = 'O';
            returnMessage = "You Missed";
        }
        else {
            if(checkForSinkShip(xPos, yPos)) {
                hitOrSink = "sank";
            }
            if(opponentBoard[xPos][yPos] == carrier) {
                opponentBoard[xPos][yPos] = 'X';
                returnMessage = "You " + hitOrSink + " your opponent's CARRIER!";
            }
            else if(opponentBoard[xPos][yPos] == battleShip) {
                opponentBoard[xPos][yPos] = 'X';
                returnMessage = "You " + hitOrSink + " your opponent's BATTLESHIP!";
            }
            else if(opponentBoard[xPos][yPos] == cruiser) {
                opponentBoard[xPos][yPos] = 'X';
                returnMessage = "You " + hitOrSink + " your opponent's CRUISER!";
            }
            else if(opponentBoard[xPos][yPos] == submarine) {
                opponentBoard[xPos][yPos] = 'X';
                returnMessage = "You " + hitOrSink + " your opponent's SUBMARINE!";
            }
            else{
                opponentBoard[xPos][yPos] = 'X';
                returnMessage = "You " + hitOrSink + " your opponent's DESTROYER!";
            }
        }
        return returnMessage;
    }

    //Not entirely sure how I am going to implement this yet
    boolean checkForSinkShip(int xPos, int yPos) {
        boolean sankShip = false;
        //
        //logic
        //
        return sankShip;
    }

    //Will return the board to the opposing user
    char[][] getBoard() {
        return board;
    }

}
