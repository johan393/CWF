package cwf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
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
    
    Center center;
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
        players = new String[people];
        try{
            out = new PrintWriter(host[0].getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(host[0].getInputStream())); 
            out.println(name);
            System.out.println("sent name" + name);
            for(int i = 0; i<people; i++){
                players[i] = in.readLine();//get the names of everybody
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
                Card[] ca = hand[0].getSelectedCards(true);//will remove the cards to be passed from the hand automatcially
                repaint();
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
            passphase = true;
            center.remove(passbutton);
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
        String[] temp = new String[2];
        String buf = "";
        
        if(people==4){
            Card[][] hands = new Card[4][13];
            for(int j =1; j<people; j++){ //j starts at 1 so that cards[0] will be this player's hand
            for(int i =0; i<13; i++){
                hands[j][i] = new Card();//sets the cards up in the other hands to be dummy cards
            }
            }
            try{
            for(int i = 0; i<13; i++){
                buf = in.readLine();
                temp = buf.split(":");
                hands[0][i] = new Card(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
            }
            }
            catch(Exception e){
                System.out.println("error reading in the dealt cards");
                e.printStackTrace();
            }
            
            hand[0]=new Hand(hands[0], 'p');
            hand[1]=new Hand(hands[1], 'l');
            hand[2]=new Hand(hands[2], 'a');
            hand[3]=new Hand(hands[3], 'r');
            this.add(hand[0], BorderLayout.SOUTH);
            this.add(hand[1], BorderLayout.WEST);
            this.add(hand[2], BorderLayout.NORTH);
            this.add(hand[3], BorderLayout.EAST);
            
        }
        setCardListeners();//this method will need to exist for both client and host, but will add wildly different behavior to the buttons
        trick=new Trick(people);
        
        
        revalidate();
        center.pos(d);
        try{
            buf = in.readLine();//to pass or not to pass: p = pass, n = no pass
        }
        catch(Exception e){
            System.out.println("could not read pass char");
            e.printStackTrace();
        }
        if(buf.equals("p")){
            pass();
        }
    }
    
    public void pass(){
       String[] temp = new String[2];
       String buf = "";
       passbutton.setText("Pass 3 Cards " + passDirection);
       passbutton.setBounds(JTimer.iposx[0]-40, JTimer.iposy[0] + 20, 160, 40);
       center.add(passbutton);
       center.repaint();
       Card [] received = new Card[3];
       
       System.out.println("awaiting new cards from pass");
       //here, the main thread should block until button thread first sends the cards to pass to the host, then receive a message from the host, sending the new cards to us from another player
       try{
       buf = in.readLine();
       temp = buf.split(":");
       received[0] = new Card(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
       
       buf = in.readLine();
       temp = buf.split(":");
       received[1] = new Card(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
       
       buf = in.readLine();
       temp = buf.split(":");
       received[2] = new Card(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
       }
       catch(Exception e){
           System.out.println("error receiving cards that were passed to me");
           e.printStackTrace();
       }
    }
    public void setCardListeners(){
       ActionListener ex = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if(passphase){
                if(((Card)e.getSource()).select){
                    ((Card)e.getSource()).deselect();
                }
                else{
                    ((Card)e.getSource()).select();
                }
            }
            else{
                //TODO TO HANDLE REGULAR PLAY
            }
        };
       };
       for(int i=0;i<hand[0].cards.length;i++){
           hand[0].cards[i].addActionListener(ex);
       }
    }
    public void paintComponent(Graphics g){
     super.paintComponent(g);
     g.drawImage(bg,0,0,null);
   }
}
