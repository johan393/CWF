package cwf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Michael
 */
public class ClientHeartsPanel extends JPanel {
    Hand [] hand;
    
    Center center;
    Image bg;
    int people;
    
    //these variables keep track of gameplay
    int[] pts;

    int roundcount;
    String[] players;
    JPanel ScoreList;
    Dimension d;
    GridBagConstraints c;
    Card playercard;
    
    JButton passbutton;
    String passDirection;
    boolean passphase;
    
    
    public ClientHeartsPanel(int people, Dimension d) {
       // super();
        //System.out.println(CWF.dir);
        this.d = d;
        roundcount = 0;
        setLayout(new java.awt.BorderLayout());
        this.people=people;
        hand = new Hand[people]; 
        center = new Center(people, d);
        bg=new ImageIcon("themes\\" + MainFrame.bgtheme + "\\bg.png").getImage();
        this.setDoubleBuffered(true);
        pts=new int[people];
        this.add(center);
        this.setVisible(true);
        
     

        this.players = players;
        
        ScoreList=new JPanel();
        ScoreList.setLayout(new GridBagLayout());
        c=new GridBagConstraints();
        c.gridy=0;
        for(int i=0; i < players.length; i++){
            c.gridx=i;
            ScoreList.add(new JLabel("   " + players[i] + "   "), c);
        }
        
        passDirection = "Left";
        passbutton= new JButton();
        Color color;
        try{
        Field field = Class.forName("java.awt.Color").getField(MainFrame.buttonColor);
        color = (Color)field.get(null);
        }
        catch(Exception e){
        color = null;
        }
        passbutton.setBackground(color);
        passbutton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            if(passphase){
                System.out.println(hand[0].getSelectedCount());
            if(hand[0].getSelectedCount()==3){
                
                
                
            }
        }   
            else{
                for(int i = 0; i<hand[0].cards.length; i++){
                    hand[0].cards[i].deselect();
                }

            }             
        }
    });
        
        
    }
}
