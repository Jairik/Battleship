package Project1;

import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;


public class battleshipController implements ActionListener{
    
    private battleshipView view;
    private battleshipModel model;
    
    //controllere contructor calls view and model constructors
    public battleshipController() throws IOException {
        view = new battleshipView();
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
            HitOrMiss = "X";
            view.updateView(position[0], position[1], HitOrMiss);
            model.updateModel(position[0], position[1], HitOrMiss); // updates the model
            if(model.shipStatus()){ // checks if ship sank. 
                view.showGameStatus(); // view function displays message
            }
        }
        else{
            HitOrMiss = "O";
        }
        //updates board accordingly in view
        view.updateView(position[0], position[1], HitOrMiss); 
    }    
}

