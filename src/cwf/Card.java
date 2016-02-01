/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.Color;
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
    boolean select;
    
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
           this.setIcon(new ImageIcon("themes\\" + MainFrame.frontTheme + "\\" + loc));
        }
        else{
           this.setIcon(new ImageIcon("themes\\" + MainFrame.theme + "\\" + x + ".png"));
        }
       
        
    }
    
    public void select(){
        this.select = true;
        this.setBorder(BorderFactory.createLineBorder(Color.yellow, 3));
    }
        
    public void deselect(){
        this.select = false;
        this.setBorder(BorderFactory.createEmptyBorder());
    }
    
    Card(char x){//creates a dummy card to hold a place
        super();
        this.setIcon(new ImageIcon("themes\\" + MainFrame.theme + "\\" + x + ".png"));
        this.setContentAreaFilled(false);
        this.setBorder(BorderFactory.createEmptyBorder());
    }
    Card(){//creates a dummy card to hold a place
        super();
        this.setContentAreaFilled(false);
        this.setBorder(BorderFactory.createEmptyBorder());
    }
}

