package Project1;

/*----------------------------------------------------------------------------------------------------------
  Authors: JJ McCauley & Will Lamuth 
  Creation Date: 3/3/24
  Last Update: 3/4/24
  Description: This class will serve as the super class to two objects: Server and Client. The constructor's
  parameter will determine which object to make, and will interact with that object on behalf of the user.
-------------------------------------------------------------------------------------------------------------*/

public class battleshipServer {
    static boolean Host;
    BServer server;
    BClient client;
    /* Constructor, which will make the host or client object and interact with them */
    battleshipServer(boolean isHost) {
        Test(server, client);
        Host = isHost;
        if(false /*Host*/) {
            server = new BServer();
        }
    }

    void Test(BServer server, BClient client) {
        server = new BServer();
        client = new BClient("127.0.0.1");
    }


}
