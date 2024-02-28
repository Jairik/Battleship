package Project1;

/*Authors: JJ McCauley & Will Lamuth
Creation Date: 2/22/24 
Last Update: 2/25/24
Description: 
User Interface: 
Notes: */

public class Battleship {
    public static void main(String[] args) {
        
        //battleshipModel model = new battleshipModel();
        battleshipView view = new battleshipView();
        battleshipController controller = new battleshipController(/*model,*/ view);
    }
}
