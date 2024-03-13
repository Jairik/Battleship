package Project1;

/*----------------------------------------------------------------------------------------------------------
  Authors: JJ McCauley & Will Lamuth 
  Creation Date: 3/3/24
  Last Update: 3/4/24
  Description: This class serves as the server object
-------------------------------------------------------------------------------------------------------------*/

//Necessary Libraries
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BServer {
    private ObjectOutputStream output; // output stream to client
    private ObjectInputStream input; // input stream from client
    private ServerSocket server; // server socket
    private Socket connection; // connection to client

    //Open the server and wait for a valid connection
    public boolean runServer() {
      boolean successfulConnection = false; 
      // set up server to receive connections; process connections
      try {
         server = new ServerSocket( 12345, 100 ); // create ServerSocket
         try {
            waitForConnection(); // wait for a connection
            getStreams(); // get input & output streams
            processConnection(); // process connection
            successfulConnection = true;
         } // end try
         catch ( EOFException eofException ) {
            System.out.println("Error connecting to server.");
         } 
      } 
      catch ( IOException ioException ) {
         ioException.printStackTrace();
      } 
      return successfulConnection;
   } 

   // send message to client
   public void sendMessage(String message) {
      try {
         output.writeObject(message);
         output.flush(); // flush output to client
      } 
      catch ( IOException ioException ) {
         System.err.println( "\nError writing object (SERVER)" );
      } 
   } 

   // recieve and return message from server
   public String getMessage() {
      String messageFromClient = "";
      try {
         messageFromClient = (String) input.readObject();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         System.err.println(e);
      }
      return messageFromClient;
   }

   //Close the conection 
   @SuppressWarnings("unused")
   private void closeConnection() {
    try {
        output.close();
        input.close();
        connection.close();
    }
    catch (IOException e) {
        e.printStackTrace();
    }
   }

   /* Waits for the connection with the client, then returns the Host name*/
   private void waitForConnection() throws IOException {
    System.out.println("Waiting for connection...");
    connection = server.accept();
    System.out.println("Connection Successful");
   }

   /* Gets the stream to send a recieve data */
   private void getStreams() throws IOException{
    //Set up an output stream for the client
    output = new ObjectOutputStream(connection.getOutputStream());
    output.flush(); //flushes the output buffer
    //set up input stream for objects
    input = new ObjectInputStream(connection.getInputStream());
   }

   //I have absolutely no clue what this does if it is necessary, so i'll just keep it until then
   private void processConnection() throws IOException{
      System.out.println("Connection Successful");
    /*try {
      message = (String) input.readObject();
    }
    catch (ClassNotFoundException e) {
      System.err.println(e);
    }
    System.out.println("Outside of process Connection"); */
   }

   /* Checks the connection status, returning false if the connection is closed */
   public boolean getConnectionStatus() {
      return !connection.isClosed();
   }
}

//Code modified:
/**************************************************************************
 * (C) Copyright 1992-2005 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *************************************************************************/