package cwf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
public class ClientHeartsPanel extends GamePanel {
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
    
    PrintWriter out;
    BufferedReader in;
    
    public ClientHeartsPanel(int people, Dimension d, String name, Socket[] host) {
       // super();
        //System.out.println(CWF.dir);
        this.d = d;
        try{
            out = new PrintWriter(host[0].getOutputStream());
            in = new BufferedReader(new InputStreamReader(host[0].getInputStream())); 
            out.println(name);
            for(int i = 0; i<people; i++){
                players[i] = in.readLine();
            }
        }
        catch(Exception e){
            System.out.println("client connection streams");
            e.printStackTrace();
        }
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

        ScoreList=new JPanel();
        ScoreList.setLayout(new GridBagLayout());
        c=new GridBagConstraints();
        c.gridy=0;
        for(int i=0; i < players.length; i++){
            c.gridx=i;
            ScoreList.add(new JLabel("   " + players[i] + "   "), c);
        }
        
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
                System.out.println("passing 3!");
                Card[] ca = hand[0].getSelectedCards(true);
                out.println(ca[0].value +":"+ ca[0].suit);//sends the three cards
                out.println(ca[1].value +":"+ ca[1].suit);
                out.println(ca[2].value +":"+ ca[2].suit);
                passphase = false;
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
    public JPanel getScoreList(){
        return ScoreList;
    }
    public void displayScores(){
        JOptionPane.showMessageDialog(this,ScoreList,"Scores",JOptionPane.PLAIN_MESSAGE);
    }
    
    public void newRound(){
        passphase=true;
        
        if(people==4){
            
            for(int i =0; i<13; i++)
            
            Card[][] hands = deck.stddeal(4);
            
            
            hand[0]=new Hand(hands[0], 'p');
            hand[1]=new Hand(hands[1], 'l');
            hand[2]=new Hand(hands[2], 'a');
            hand[3]=new Hand(hands[3], 'r');
            this.add(hand[0], BorderLayout.SOUTH);
            this.add(hand[1], BorderLayout.WEST);
            this.add(hand[2], BorderLayout.NORTH);
            this.add(hand[3], BorderLayout.EAST);
            
        }
        setCardListeners();
        trick=new Trick(people);
        
        for(int i=0;i<people;i++){
            piles[i].clear();
        }
        
        revalidate();
        center.pos(d);
        heartsbroken = false;
        start = false;
        qplayed = false;

        if(!passDirection.equals("K")){
        pass();
        }
        else{
        passphase = false;
        passDirection = "Left";    
        }
}
