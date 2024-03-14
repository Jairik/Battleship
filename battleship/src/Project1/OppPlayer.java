package Project1;

import java.util.Random;

public class OppPlayer {
    private char[][] oppBoard;
    private int boardWidth = 10;
    private int boardHeight = 10;
    battleshipModel model = new battleshipModel();
    battleshipView view;

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

    int carrierRemaining = 5;
    int battleshipRemaining = 4;
    int cruiserRemaining = 3;
    int submarineRemaining = 3;
    int destroyerRemaining = 2;

    int shipsRemaining = 5;

    OppPlayer(){
        System.out.println("this is opponent");
        //initializeBoard();
        oppBoard = new char[boardHeight][boardWidth];
        randomlySetBoard();
        printBoard();
    }

    public void initializeBoard(){
        for(int i = 0; i < boardHeight; i++){
            for(int j = 0; j < boardWidth; j++){
                oppBoard[i][j] = ' ';
            }
        }
    }

    void randomlyPlaceShip(int shipSize, char currentShip) {
        boolean placeAgain = true;
        while(placeAgain) {
            int ranX = (int)(Math.random() * (boardWidth)); //Getting a random X coordinate
            int ranY = (int)(Math.random() * (boardHeight)); //Getting a random Y coordinate
            int dir = (int)(Math.random() * 2); //Getting a random int to represent direction
            int iterator = 0; //Used to iterate across the board to check if spots are valid or taken
            int shipPlacedCount = 0;
            //System.out.println("In randomlyPlaceShip DIR: " + dir + " ship: " + currentShip);
            if (dir == 0) { //Move right across the board
                //Check each spot to see if the spot is empty or out of bounds
                while(iterator < shipSize) {
                    if((ranX+iterator) < boardWidth && oppBoard[ranX+iterator][ranY] == ' ') {
                        shipPlacedCount++;
                    }
                    iterator++;
                }             
            }
            else { //Move down the board
                //Check each spot to see if the spot is empty or out of bounds
                while(iterator < shipSize) {
                    if((ranY+iterator) < boardHeight && oppBoard[ranX][ranY+iterator] == ' ') {
                        shipPlacedCount++;
                    }
                    iterator++;
                }
            }
            if(shipPlacedCount == shipSize) {
                placeAgain = false;
                for(int i = 0; i < shipSize; i++) {
                    if (dir == 0) {
                        oppBoard[ranX+i][ranY] = currentShip;
                    } 
                    else {
                        oppBoard[ranX][ranY+i] = currentShip;
                    }
                }
            }
        }
    }
    void randomlySetBoard() {
        initializeBoard();
        randomlyPlaceShip(carrierSize, carrier);
        randomlyPlaceShip(battleShipSize, battleShip);
        randomlyPlaceShip(cruiserSize, cruiser);
        randomlyPlaceShip(submarineSize, submarine);
        randomlyPlaceShip(destroyerSize, destroyer);
    }

    public void printBoard(){
        for(int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                System.out.print(oppBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    //passes in user board and 'X'
    public void opponentShoot(char[][] opponent, char character) {
        Random rand = new Random();
        int row = rand.nextInt(opponent.length);
        int col = rand.nextInt(opponent.length);
        while(!model.checkForValidShot(row, col)){
            row = rand.nextInt(opponent.length);
            col = rand.nextInt(opponent.length);
        }
        if(opponent[row][col] == ' '){
            character = 'O';
        }
        else if(opponent[row][col] == 'c'){
            character = 'X';
        }
        else if(opponent[row][col] == 'b'){
            character = 'X';
        }
        else if(opponent[row][col] == 'r'){
            character = 'X';
        }
        else if(opponent[row][col] == 's'){
            character = 'X';
        }
        else if(opponent[row][col] == 'd'){
            character = 'X';
        }
        else{
            character = 'O';
        }
        opponent[row][col] = character;
        //model.setModelBoolean(true);
    }


    char[][] getOppBoard() {
        return oppBoard;
    }

    boolean checkForSinkShipOpponentBoard() {
        boolean sank = false;
        //int numOfShips = 5;
        //char shipArr[] = {carrier, battleShip, cruiser, submarine, destroyer};
        int shipHitsRemainingArr[] = {carrierRemaining, battleshipRemaining, cruiserRemaining, submarineRemaining, destroyerRemaining};
        for(int i = 0; i < shipHitsRemainingArr.length; i++) {
            if(shipHitsRemainingArr[i] == 0) {
                sank = true;
                shipsRemaining--;
                shipHitsRemainingArr[i] = -1; // decrements ship amount after each dink
                //break; //skip the rest of the iterations
            }
        }
        return sank;
    }

    public boolean checkForValidShot(int x, int y) {
        if(oppBoard[x][y] == 'X' || oppBoard[x][y] == 'O'){
           return false;
        }
        return true;
   }

String determineHit(int xPos, int yPos) {
    String hit = "";

    if(oppBoard[xPos][yPos] == ' ') {
        oppBoard[xPos][yPos] = 'O';
        hit = "O";
    }
    else if(oppBoard[xPos][yPos] == carrier) {
        oppBoard[xPos][yPos] = 'X';
        hit = "X";
        carrierRemaining--;
        if(checkForSinkShipOpponentBoard()) {
            hit = "Carrier";
        }
    }
    else if(oppBoard[xPos][yPos] == battleShip) {
        oppBoard[xPos][yPos] = 'X';
        hit = "X";
        battleshipRemaining--;
        if(checkForSinkShipOpponentBoard()) {
            hit = "Battleship";
        }
    }
    else if(oppBoard[xPos][yPos] == cruiser) {
        oppBoard[xPos][yPos] = 'X';
        hit = "X";
        cruiserRemaining--;
        if(checkForSinkShipOpponentBoard()) {
            hit = "Cruiser";
        }
    }
    else if(oppBoard[xPos][yPos] == submarine) {
        oppBoard[xPos][yPos] = 'X';
        hit = "X";
        submarineRemaining--;
        if(checkForSinkShipOpponentBoard()) {
            hit = "Submarine";
        }
    }
    else if(oppBoard[xPos][yPos] == destroyer){
        oppBoard[xPos][yPos] = 'X';
        hit = "X";
        destroyerRemaining--;
        if(checkForSinkShipOpponentBoard()) {
            hit = "Destroyer";
        }
    }
    else{
        oppBoard[xPos][yPos] = 'O';
        hit = "O";
    }
    //model.setModelBoolean(false);
    return hit;
}

public boolean isWin(){
    for(int i = 0; i < boardHeight; i++) {
        for (int j = 0; j < boardWidth; j++) {
            if(oppBoard[i][j] == 'c' || oppBoard[i][j] == 'b'|| oppBoard[i][j] == 'r'|| oppBoard[i][j] == 'd' || oppBoard[i][j] == 's'){
                return false;
            }
        }
        
    }
    return true;
}



    
}
