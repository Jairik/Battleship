package Project1;

import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
//import org.w3c.dom.events.MouseEvent;
import javax.swing.*;


public class battleshipController implements ActionListener{
    //battleshipModel model;
    private battleshipView view;
    //private JButton[][] button2;
    public battleshipController() throws IOException {
        //this.view = view;
        //this.model = model;
        //button2 = new JButton[10][10];
        view = new battleshipView();
        fireCannon();
        
        
    }
    public void fireCannon(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                //JButton btn = new JButton();
                view.getButton(i, j).addActionListener(this);
            }
        }
    }


@Override
public void actionPerformed(ActionEvent e) {
    JButton clickedButton = (JButton)e.getSource();
        //finds clicked button position
        int[] position = view.buttonPosition(clickedButton);
        view.updateView(position[0], position[1]); 
    }    
}

