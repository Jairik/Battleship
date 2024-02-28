package Project1;

import java.io.IOException;

public class battleshipController {
    //battleshipModel model;
    battleshipView view;
    public battleshipController(battleshipModel model, battleshipView view) throws IOException {
        //this.view = view;
        //this.model = model;
        view = new battleshipView();
        view.fireCannon();
        
    }
        
}

