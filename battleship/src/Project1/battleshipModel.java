package Project1;

/*Authors: JJ McCauley & Will Lamuth
Creation Date: 2/24/24 
Last Update: 3/3/24 */

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
    //boolean turn; //Boolean may not be the best dataType

    //Remaining shots needed to sink the given ship 
    int carrierRemaining = carrierSize;
    int battleshipRemaining = battleShipSize;
    int cruiserRemaining = cruiserSize;
    int submarineRemaining = submarineSize;
    int destroyerRemaining = destroyerSize;
    
    //Remaining shots needed to sink opponent ships
    int carrierRemainingOpponent = carrierSize;
    int battleshipRemainingOpponent = battleShipSize;
    int cruiserRemainingOpponent = cruiserSize;
    int submarineRemainingOpponent = submarineSize;
    int destroyerRemainingOpponent = destroyerSize;

    int shipsRemaining = 5; // variable to track how many of the user's ships remain
    int shipsRemainingOpponent = 5; // variable to track how many of the opponent's ships remain

    boolean setModel;
    //Constructor to make board and declare currentTurn
    public battleshipModel() {
        board = new char[boardHeight][boardWidth];
        opponentBoard = new char[boardHeight][boardWidth];
        //randomlySetBoard();
        //initBoard();
    }
    // will - called in controller to check valid shot
    /*Validates a user's shot, returning true if the shot will fall on an empty place on the array*/
    public boolean checkForValidShot(int x, int y) {
         if(board[x][y] == 'X' || board[x][y] == 'O'){
            return false;
         }
         return true;
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
            System.out.println("In randomlyPlaceShip DIR: " + dir + " ship: " + currentShip);
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
    Sink: <shipName>*/    
    /* Determine if a ship has been hit
     */
    String determineHitUserBoard(int xPos, int yPos) {
        String hit;
        boolean shipSank = checkForSinkShipUserBoard(xPos, yPos);

        if(board[xPos][yPos] == ' ') {
            board[xPos][yPos] = 'O';
            hit = "O";
        }
        else if(opponentBoard[xPos][yPos] == ' ') {
            opponentBoard[xPos][yPos] = 'O';
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

    String determineHitOpponentBoard(int xPos, int yPos) {
        String hit;
        boolean shipSank = checkForSinkShipOpponentBoard(xPos, yPos);

        if(opponentBoard[xPos][yPos] == ' ') {
            opponentBoard[xPos][yPos] = 'O';
            hit = "O";
        }
        else {
            if(opponentBoard[xPos][yPos] == carrier) {
                opponentBoard[xPos][yPos] = 'X';
                hit = "X";
                carrierRemainingOpponent--;
                if(shipSank) {
                    hit = "Carrier";
                }
            }
            else if(opponentBoard[xPos][yPos] == battleShip) {
                opponentBoard[xPos][yPos] = 'X';
                hit = "X";
                battleshipRemainingOpponent--;
                if(shipSank) {
                    hit = "Battleship";
                }
            }
            else if(opponentBoard[xPos][yPos] == cruiser) {
                opponentBoard[xPos][yPos] = 'X';
                hit = "X";
                cruiserRemainingOpponent--;
                if(shipSank) {
                    hit = "Cruiser";
                }
            }
            else if(opponentBoard[xPos][yPos] == submarine) {
                opponentBoard[xPos][yPos] = 'X';
                hit = "X";
                submarineRemainingOpponent--;
                if(shipSank) {
                    hit = "Submarine";
                }
            }
            else{
                opponentBoard[xPos][yPos] = 'X';
                hit = "X";
                destroyerRemainingOpponent--;
                if(shipSank) {
                    hit = "Destroyer";
                }
            }
        }
        return hit;
    }

    /* Check the user board for a sunken ship, returning true if it was sank */
    boolean checkForSinkShipUserBoard(int xPos, int yPos) {
        boolean sank = false;
        int numOfShips = 5;
        //char shipArr[] = {carrier, battleShip, cruiser, submarine, destroyer};
        int shipHitsRemainingArr[] = {carrierRemaining, battleshipRemaining, cruiserRemaining, submarineRemaining, destroyerRemaining};
        for(int i = 0; i < numOfShips; i++) {
            if(shipHitsRemainingArr[i] == 1) {
                sank = true;
                shipsRemaining--; // decrements ship amount after each dink
                break; //skip the rest of the iterations
            }
        }
        return sank;
    }

    /* Check the user board for a sunken ship, returning true if it was sank */
    boolean checkForSinkShipOpponentBoard(int xPos, int yPos) {
        boolean sank = false;
        int numOfShips = 5;
        //char shipArr[] = {carrier, battleShip, cruiser, submarine, destroyer};
        int shipHitsRemainingArr[] = {carrierRemainingOpponent, battleshipRemainingOpponent, cruiserRemainingOpponent, submarineRemainingOpponent, destroyerRemainingOpponent};
        for(int i = 0; i < numOfShips; i++) {
            if(shipHitsRemainingArr[i] == 1) {
                sank = true;
                shipsRemainingOpponent--; // decrements ship amount after each dink
                break; //skip the rest of the iterations
            }
        }
        return sank;
    }

    //Returns the board back to the controller
    char[][] getUserBoard() {
        return board;
    }

    //called in controller to signal win
    boolean isWinUser(){
        return (shipsRemaining == 0);
    }

    boolean isWinOpponent(){
        return (shipsRemainingOpponent == 0);
    }

    /* Receiving the opponents board and setting it */
    void setOppBoard(char[][] oppBoard) {
        opponentBoard = oppBoard;
    }

    //each ship class will call this function to update model
    void setModel(int x, int y, String shipCharacter){
        board[y][x] = shipCharacter.charAt(0);
    }

    void printBoard(){
        for(int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    //upon receiving the shot, will update the array
    void receiveShot(int x, int y, String c) {
        char hitOrMissChar = 'X';
        if(c == "O") {
            hitOrMissChar = 'O';
        }
        board[x][y] = hitOrMissChar;
    }

    void setModelBoolean(boolean modelset){
        setModel = modelset;
    }

    public boolean modelIsSet(){
        return setModel;
    }

}
