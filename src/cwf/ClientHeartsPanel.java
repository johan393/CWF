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
    final Object lock;
    int playerpos;
    
    JButton passbutton;
    String passDirection;
    boolean passphase;
    
    PrintWriter out;
    BufferedReader in;
    String tristate;
    
    public ClientHeartsPanel(int people, Dimension d, String name, Socket[] host) {
       // super();
        //System.out.println(CWF.dir);
        this.d = d;
        lock = new Object();
        players = new String[people];
        try{
            out = new PrintWriter(host[0].getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(host[0].getInputStream())); 
            out.println(name);
            System.out.println("sent name" + name);
            for(int i = 0; i<people; i++){
                players[i] = in.readLine();//get the names of everybody
                if(players[i].equals(name)){
                    playerpos=i;
                }
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
        center = new Center(people, d,players, playerpos);
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
                out.println(ca[0].value +":"+ ca[0].suit);//sends the three cards
                out.println(ca[1].value +":"+ ca[1].suit);
                out.println(ca[2].value +":"+ ca[2].suit);
                hand[0].revalidate();
                passphase = false;
                ((JButton)e.getSource()).setEnabled(false);
                synchronized(lock){
                    System.out.println(" block   ");
                    try{
                        lock.notify();
                    }
                    catch(Exception ex){
                        System.out.println("could not notify passing wait");
                    }
                }
            }
            }
            else{
                for(int i = 0; i<hand[0].cards.length; i++){
                    hand[0].cards[i].deselect();
                }
            
            center.remove(passbutton);
            synchronized(lock){
                    try{
                        lock.notify();
                    }
                    catch(Exception ex){
                        System.out.println("could not notify passing ok wait");
                    }
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
        String[] temp = new String[2];
        String buf = "";
        
        if(people==4){
            Card[][] hands = new Card[4][13];
            try{
            for(int i = 1;i<people;i++){
                for(int j = 0;j<13;j++){
                   hands[i][j] = new Card(); 
                }
                
            }
            for(int i = 0; i<13; i++){
                buf = in.readLine();
                temp = buf.split(":");
                if(temp[0].equals("13")){
                    hands[0][i] = new Card(0, Integer.parseInt(temp[1]));
                    hands[0][i].value = 13;
                }
                else{
                    hands[0][i] = new Card(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
                }
            }
            }
            catch(Exception e){
                System.out.println("error reading in the dealt cards");
                e.printStackTrace();
            }
            if(hand[0]!=null){
            this.remove(hand[0]);
            this.remove(hand[1]);
            this.remove(hand[2]);
            this.remove(hand[3]);
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
        
        
        revalidate();
        center.pos(d);
        try{
            buf = in.readLine();//to pass or not to pass: p = pass, n = no pass
        }
        catch(Exception e){
            System.out.println("could not read pass char");
            e.printStackTrace();
        }
        if(!buf.equals("n")){
            passDirection = buf;
            pass();
        }
        else{
            passphase=false;
        }
        proceed();
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
       synchronized(lock){//wait for player to select the cards to pass
            try{
            lock.wait();
            }
            catch(Exception e){
                System.out.println("error waiting for passbutton");
            }
        }
       //here, the main thread should block until button thread first sends the cards to pass to the host, then receive a message from the host, sending the new cards to us from another player
       try{
       buf = in.readLine();
       temp = buf.split(":");
       if(temp[0].equals("13")){
         received[0] = new Card(0, Integer.parseInt(temp[1]));
         received[0].value=13;
       }
       else{
         received[0] = new Card(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
       }
       received[0].select();
       
       buf = in.readLine();
       temp = buf.split(":");
       if(temp[0].equals("13")){
         received[1] = new Card(0, Integer.parseInt(temp[1]));
         received[1].value=13;
       }
       else{
         received[1] = new Card(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
       }
       received[1].select();
       
       
       buf = in.readLine();
       temp = buf.split(":");
       if(temp[0].equals("13")){
         received[2] = new Card(0, Integer.parseInt(temp[1]));
         received[2].value=13;
       }
       else{
         received[2] = new Card(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
       }
       
       received[2].select();
       
       }
       catch(Exception e){
           System.out.println("error receiving cards that were passed to me");
           e.printStackTrace();
       }
       
       for(int i = 0; i<3; i++){
                    received[i].select();
                    received[i].addActionListener(new ActionListener() {
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
                                try{
                                    synchronized(lock){
                                        playercard = (Card) e.getSource();
                                        lock.notify();
                                    }
                                }
                                catch(Exception E){
                                    System.out.println("Button failed to notify main thread");  
                                }
                            }
                        }});
                }
       
       
       hand[0].addCards(received);
       
       passbutton.setText("OK");
       passbutton.setEnabled(true);
       synchronized(lock){//wait for player to select the cards to pass
            try{
            lock.wait();
            }
            catch(Exception e){
                System.out.println("error waiting for passbutton");
            }
        }
       Card[][] hands = new Card[4][13];
       try{
       for(int i = 0; i<people; i++){
           if((i+(4-playerpos))%4!=0){
           for(int j = 0; j<13; j++){
               buf = in.readLine();
               temp = buf.split(":");
               if(temp[0].equals("13")){
                     hands[i][j] = new Card(0,Integer.parseInt(temp[1]) );
                     hands[i][j].value=13;
               }
               else{
                    hands[i][j] = new Card(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]) );
                    if(temp[0].equals("0")){
                        hands[i][j].value = 13;
                    }
               }
           }
           }
       }
       this.remove(hand[1]);
       this.remove(hand[2]);
       this.remove(hand[3]);
       hand[1]=new Hand(hands[(1+playerpos)%4], 'l');//l
       hand[2]=new Hand(hands[(2+playerpos)%4], 'a');//a
       hand[3]=new Hand(hands[(3+playerpos)%4], 'r');//r
       this.add(hand[1], BorderLayout.WEST);
       this.add(hand[2], BorderLayout.NORTH);
       this.add(hand[3], BorderLayout.EAST);
       this.revalidate();
       }
       catch(Exception e){
           System.out.println("cant read in deal after pass");
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
                try{
                    synchronized(lock){
                        playercard = (Card) e.getSource();
                        lock.notify();
                    }
                }
                catch(Exception E){
                      System.out.println("Button failed to notify main thread");  
                }
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
    
    public void proceed(){
        boolean done = false;
        String buf;
        int player;
        String[] temp = new String[2];
        while(!done){
            try{
                buf = in.readLine();
                if(buf.equals("g")){//it is the player's turn
                    System.out.println("rcvd g");
                    try{
                        synchronized(lock){
                            lock.wait();
                        }
                    }
                    catch(Exception ex){
                        System.out.println("Error in proceed synchronization awaiting client response");
                        ex.printStackTrace();
                    }
                    out.println(playercard.value + ":" + playercard.suit);
                    System.out.println("played" + playercard.loc);
                }
                else if(buf.equals("p")){//card played, time to show the user
                     System.out.println("rcvd p");
                    buf = in.readLine();
                    player = Integer.parseInt(buf);
                    buf = in.readLine();
                    temp = buf.split(":");
                    hand[(player+(4-playerpos))%4].playCard(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
                    if(temp[0].equals("13")){
                    center.playCard(new Card(0,Integer.parseInt(temp[1])),(player+(4-playerpos))%4);    
                    }
                    else{
                    center.playCard(new Card(Integer.parseInt(temp[0]),Integer.parseInt(temp[1])),(player+(4-playerpos))%4);
                    }
                }
                else if(buf.equals("t")){//trick was taken
                     System.out.println("rcvd t");
                    buf = in.readLine();
                    player = Integer.parseInt(buf);
                    center.takeTrick((player+(4-playerpos))%4);
                }
                else if(buf.equals("d")){//end of round
                     System.out.println("rcvd d");
                    doRoundEnd();
                }
            }
            catch(Exception e){
                System.out.println("error in proceeding client");
                e.printStackTrace();
            }
        }
    }
    
    public void doRoundEnd(){
        roundcount++;
        c.gridy=roundcount;
        String buf;
        try{
        for(int i=0;i<people;i++){
         c.gridx=i;
         buf = in.readLine();
         ScoreList.add(new JLabel(buf), c);
        }
        buf = in.readLine();
        if(buf.equals("m")){
            System.out.println("game is over");
            JOptionPane.showMessageDialog(this,ScoreList,"Scores",JOptionPane.PLAIN_MESSAGE);
        }
        else{
            System.out.println("game continues");
            JOptionPane.showMessageDialog(this,ScoreList,"Scores",JOptionPane.PLAIN_MESSAGE);
            newRound();
        }
        }
        catch(Exception e){
            System.out.println("error receiving scores");
            e.printStackTrace();
        }
        
        
    }
}
