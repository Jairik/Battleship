package Project1;

import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class battleshipController implements ActionListener{
    
    private battleshipView view;
    private battleshipModel model;
    
    //controllere contructor calls view and model constructors
    public battleshipController() throws IOException {
        model = new battleshipModel(); 
        char[][] testArr = model.getBoard(); //testing
        view = new battleshipView(testArr); //parameter for testing
        model = new battleshipModel();
        fireCannon();
        
        
    }
    //adds a actionlistener to every button
    public void fireCannon(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                view.getButton(i, j).addActionListener(this);
            }
        }
    }

//button action for firing
@Override
public void actionPerformed(ActionEvent e) {
    JButton clickedButton = (JButton)e.getSource();
        //finds clicked button position
        int[] position = view.buttonPosition(clickedButton);
        String HitOrMiss = "";
        //if statement calls checkforvalidshot() from model if true hit = X else miss = O
        if(model.checkForValidShot(position[0], position[1])){
            HitOrMiss = model.determineHit(position[0], position[1]); //updates model 
            if(HitOrMiss == "X") {
                //playHitSound();
            }
            else if(HitOrMiss == "O") {
                //playMissSound();
            }
            else { //Ship has been sank
                //HitOrMiss now stores the String of what Ship has been sank.
                //Can update the label containing the ship here
                view.showGameStatus(); // view function displays message
            }
            view.updateView(position[0], position[1], HitOrMiss);
        }
        else {
            //User did not shoot a valid shot, must add logic to have them try again
        }
        //updates board accordingly in view
        view.updateView(position[0], position[1], HitOrMiss); 
    } 
    
}

