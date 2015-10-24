/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

/**
 *
 * @author BeerSmokinGenius
 */
public class Card extends javax.swing.JButton{
    
    int value;
    int suit;
    String loc;
    char disp;
    
    Card(int val, int s){
        super();
        this.value = val;
        this.suit = s;
        loc = Integer.toString(suit+1) + "_" + Integer.toString(value+1) + ".png";  
        this.setContentAreaFilled(false);
        this.setBorder(BorderFactory.createEmptyBorder());
    }
    
    void setCard(char x){
        disp=x;
        if(x=='p'){
           this.setIcon(new ImageIcon(loc));
        }
        else if(x=='l'){
           this.setIcon(new ImageIcon("back-blue-l.png"));
        }
        else if(x=='t'){
           this.setIcon(new ImageIcon("back-blue-t.png"));
        }
        else if(x=='r'){
           this.setIcon(new ImageIcon("back-blue-r.png"));
        }
        else if(x=='m'){
           this.setIcon(new ImageIcon("back-blue-m.png"));
        }
        else if(x=='g'){
           this.setIcon(new ImageIcon("back-blue-g.png"));
        }
        else if(x=='u'){
           this.setIcon(new ImageIcon("back-blue-u.png"));
        }
        else if(x=='c'){
           this.setIcon(new ImageIcon("back-blue-c.png"));
        }
        else if(x=='j'){
           this.setIcon(new ImageIcon("back-blue-j.png"));
        }
        else if(x=='f'){
           this.setIcon(new ImageIcon("back-blue-f.png"));
        }
        else if(x=='o'){
           this.setIcon(new ImageIcon("back-blue-o.png"));
        }
        else if(x=='z'){
           this.setIcon(new ImageIcon("back-blue-z.png"));
        }
        else if(x=='a'){
           this.setIcon(new ImageIcon("back-blue-a.png"));
        }
        
    }
}

