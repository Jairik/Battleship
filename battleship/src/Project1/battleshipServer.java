package Project1;

/*----------------------------------------------------------------------------------------------------------
  Authors: JJ McCauley & Will Lamuth 
  Creation Date: 3/3/24
  Last Update: 3/5/24
  Description: This class will serve as the super class to two objects: Server and Client. The constructor's
  parameter will determine which object to make, and will interact with that object on behalf of the user.
-------------------------------------------------------------------------------------------------------------*/

/* GUIDE ON HOW TO USE THIS JAWN:
 * For implementation, this class bascially has 4 important functions that will have to be called in controller:
 * send(Char[][] board) >>> Used to send the char array to the opponent
 * send(int x, int y) >>> Used to pass the int coordinates to the opponent
 * receiveOpponentBoard() >>> Used to get board from opponent, returns a char[][]
 * receiveOpponentCoordinates() >>> Used to get coordinates from opponent, returns an int[][]
 * 
 * Note: I did all the conversions within the function, however they likelihood of them not working is high. Therefore,
 * it isnt working as intended. I would verify the logic and stuff
 */



import javax.swing.SwingUtilities; //Used for invokeLater() Function (Potentially)

public class battleshipServer {
    static boolean Host;
    BServer server;
    BClient client;
    String hostIP = "127.0.0.1";
    /* Constructor, which will make the host or client object and interact with them */
    battleshipServer(boolean isHost) {
        Host = isHost;
        if(Host) {
            server = new BServer();
        }
        else {
            client = new BClient(hostIP);
        }
    }

    public String getIP() {
        return hostIP;
    }

    /* Runs either the client or host, returning whether a connection has been established or not */
    public boolean Connect() {
        boolean successfulConnection = false;
        if(Host) {
            successfulConnection = server.runServer();
        }
        else {
            successfulConnection = client.runClient();
        }
        return successfulConnection;
    }

    /* Sends two coordinates to the opponent, of which is converted to a String message and sent. 
     * This message will be converted back to ints upon receiving. */
    public void send(int x, int y) {
        String data = Integer.toString(x);
        data += Integer.toString(y);
        if(Host) {
            server.sendMessage(data);
        }
        else {
            client.sendMessage(data);
        }
    }

    /* Overloaded function to send the board to the opponent. The board will be converted into a
     * String message, of which will be converted back upon receiving. */
    public void send(char[][] board) {
        String data = "";
        // Convert char board to String array
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                data += Character.toString(board[i][j]);
            }
        }

        if(Host) {
            server.sendMessage(data);
        }
        else {
            client.sendMessage(data);
        }
    }

    /* Will receive the board from the opponent, convert it to a char[][], then
     * return it to the Controller */
    public char[][] receiveBoard() {
        char[][] oppBoard = new char[10][10];
        String data;
        int currentIndex;
        if(Host) {
            data = server.getMessage();
        }
        else {
            data = client.getMessage();
        }
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                currentIndex = (i*10)+j;
                oppBoard[i][j] = data.charAt(currentIndex);
            }
        }
        return oppBoard;
    }

    public int[] receiveCoordinates() {
        int[] coordinates = new int[2];
        char coordinateChar1, coordinateChar2;
        String data;
        if(Host) {
            data = server.getMessage();
        }
        else {
            data = client.getMessage();
        }
        //Seperate the coordinates into two chars
        coordinateChar1 = data.charAt(0);
        coordinateChar2 = data.charAt(1); 
        //Convert the chars to ints via ascii conversions
        coordinates[0] = ((int)(coordinateChar1) - 48);
        coordinates[1] = ((int)(coordinateChar1) - 48);

        return coordinates;
    }

    //Returns whether the user is the host or not
    public boolean isHost() {
        return Host;
    }
}
