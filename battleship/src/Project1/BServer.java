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
    //Variables go here
    private ObjectOutputStream output; // output stream to client
    private ObjectInputStream input; // input stream from client
    private ServerSocket server; // server socket
    private Socket connection; // connection to client

    //Open the server and wait for a valid connection
    public void runServer() {
      // set up server to receive connections; process connections
      try {
         server = new ServerSocket( 12345, 100 ); // create ServerSocket

         while ( true ) {
            try {
               waitForConnection(); // wait for a connection
               getStreams(); // get input & output streams
               processConnection(); // process connection
            } // end try
            catch ( EOFException eofException ) {
               //Display to JFRame in view that connection has been terminated
            } // end catch
            finally {
               closeConnection(); //  close connection
            } 
         } 
      } 
      catch ( IOException ioException ) {
         ioException.printStackTrace();
      } 
   } 

   // send message to client
   private void sendData(String message) {
      try {
         output.writeObject( "SERVER>>> " + message );
         output.flush(); // flush output to client
      } 
      catch ( IOException ioException ) {
         System.err.println( "\nError writing object (SERVER)" );
      } 
   } 

   //Close the conection 
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
    connection = server.accept();
    //return(connection.getInetAddress().getHostName());
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
    String message = "Connection could not process";
    sendData("Connection Successful");
    try {
      message = (String) input.readObject();
    }
    catch (ClassNotFoundException e) {
      System.err.println(e);
    }
   }

}

//Code modified:
/**************************************************************************
 * (C) Copyright 1992-2005 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *************************************************************************/