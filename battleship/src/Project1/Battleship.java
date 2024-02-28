package Project1;

import java.io.IOException;

/*Authors:JJ McCauley & Will Lamuth
Creation Date: 2/22/24 
Last Update: 2/25/2
Description: 
User Interface: 
Notes: */

public class Battleship {
    public static void main(String[] args) throws IOException {
        
        battleshipModel model = new battleshipModel();
        battleshipView view = new battleshipView();
        battleshipController controller = new battleshipController(model, view);
    }
}
