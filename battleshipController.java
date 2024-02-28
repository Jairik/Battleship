package Project1;

public class battleshipController {
    //battleshipModel model;
    battleshipView view;
    public battleshipController(/*battleshipModel model,*/ battleshipView view) {
        //this.view = view;
        //this.model = model;
        view = new battleshipView();
        view.fireCannon();
        
    }
        
}

