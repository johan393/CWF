/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.currentThread;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Timer;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author BeerSmokinGenius
 */
public class HeartsPanel extends javax.swing.JPanel {

    /**
     * Creates new form HeartsPanel
     */
    //these variables are part of game setup
    Hand [] hand;
    Deck deck;
    int people;
    Center center;
    Image bg;
    Timer t;
    
    //these variables keep track of gameplay
    ArrayList<Card>[] piles;
    int[] pts;
    Trick trick;
    int person;
    int roundcount;
    String[] players;
    JPanel ScoreList;
    Dimension d;
    GridBagConstraints c;
    final Object lock;
    Card playercard;
    
    Card[][] passcards;
    JButton passbutton;
    String passDirection;
    boolean passphase;
    
    //these variables keep track of AI data
    boolean start;
    boolean heartsbroken;
    boolean qplayed;
    
    
    public HeartsPanel(int people, Dimension d, String[] players) {
       // super();
        initComponents();
        lock = new Object();
        this.d = d;
        roundcount = 0;
        setLayout(new java.awt.BorderLayout());
        this.people=people;
        hand = new Hand[people]; 
        center = new Center(people, d);
        bg=new ImageIcon("themes\\" + MainFrame.bgtheme + "\\bg.png").getImage();
        this.setDoubleBuffered(true);
        piles=new ArrayList[people];
        pts=new int[people];
        for(int i=0;i<people;i++){
            piles[i]=new ArrayList<>();
        }
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
        passbutton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            if(passphase){
                System.out.println(hand[0].getSelectedCount());
            if(hand[0].getSelectedCount()==3){
                passcards[0]=hand[0].getSelectedCards();
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
                synchronized(lock){
                    try{
                        lock.notify();
                    }
                    catch(Exception ex){
                        System.out.println("could not notify passing wait");
                    }
                }
            }
        }
    });
        
        
        
    }
   public void newRound(){
        deck=new Deck();
        deck.shuffle();
        deck.empVal(0,13);
        passphase=true;
        
        if(people==4){
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
        
        person = getFirstPlayer();///the person to lead the two of clubs
        
        pass();
        
        proceed();
    }
   
   public void pass(){
       passbutton.setText("Pass 3 Cards " + passDirection);
       passbutton.setBounds(JTimer.iposx[0]-40, JTimer.iposy[0] + 20, 160, 40);
       center.add(passbutton);
       
       passcards = new Card[people][3];
       
       for(int i = 1; i<people; i++){//everybody except the player
           passcards[i]=getPassCards(i);
       }
       
        synchronized(lock){//wait for player to select the cards to pass
            try{
                System.out.println("bwait1");
            lock.wait();
            System.out.println("bwait2");
                
            }
            catch(Exception e){
                System.out.println("error waiting for passbutton");
            }
        }
        System.out.println("MMM"+passcards[0][0].suit);
        passphase = false;
        passcards[0][0].deselect();
        passcards[0][1].deselect();
        passcards[0][2].deselect();
        
        if(people == 4){
            if(passDirection.equals("Left")){
                hand[1].addCards(passcards[0]);
                hand[2].addCards(passcards[1]);
                hand[3].addCards(passcards[2]);
                passcards[3][0].select();
                passcards[3][1].select();
                passcards[3][2].select();
                hand[0].addCards(passcards[3]);
                passDirection = "right";
                passbutton.setText("OK");
            }
        }
        synchronized(lock){//wait for player to check out new cards
            try{
            lock.wait();
            }
            catch(Exception e){
                System.out.println("error waiting for passbutton");
            }
        
            center.remove(passbutton);
   }
   }
   public void paintComponent(Graphics g){
     super.paintComponent(g);
     g.drawImage(bg,0,0,null);

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
                if(person==0){
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
            }
        };
       };
       for(int i=0;i<hand[0].cards.length;i++){
           hand[0].cards[i].addActionListener(ex);
       }
    }
   
   public int getStart(int x){
       for(int i=0;i<people;i++){
           for(int j=0;j<(52/people);j++){
              if(hand[i].cards[j].suit==1&&hand[i].cards[j].value==x){
                 return i;  
              }
           }
       }
       return getStart(x+1);
   }    
   public int getFirstPlayer(){
       return getStart(1);
   }

 
 public void doRoundEnd(){
     roundcount++;
     c.gridy=roundcount;
     int pt;
     boolean owned = false;
     for(int i=0;i<people;i++){
         this.remove(hand[i]);
         pt = countPoints(piles[i]);
         if(pt==26){
            owned = true;
         }
         pts[i]=pts[i]+ pt;
     }
     if(owned == true){
          for(int i=0;i<people;i++){
              pt = countPoints(piles[i]);
              if(pt==26){
                  pts[i] = pts[i] - 26;//removes 26 from the person who got them all 
              }
              else{
                  pts[i] = pts[i] + 26;//add 26 to those that were owned
              }
          }
     }
     for(int i=0;i<people;i++){
         c.gridx=i;
         ScoreList.add(new JLabel(Integer.toString(pts[i])), c);
     }
     
     JOptionPane.showMessageDialog(this,ScoreList,"Scores",JOptionPane.PLAIN_MESSAGE);
     newRound();
     
 }
 
 public int countPoints(ArrayList pile){
     int points=0;
     ListIterator<Card> i= pile.listIterator();
     Card card;
     while(i.hasNext()){
        card=i.next();
        if(card.suit==0&&card.value==11){
            points=points+13;
        }
        else if(card.suit==3){
            points=points+1;
        }
     }
     return points;
 }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(4000, 4000));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

public void proceed(){
    boolean done = false;
    while(!done){
        if(trick.trickStatus()==people){//if trick is complete
            person = trick.highestPlayer(-1);
            piles[person].addAll(Arrays.asList(trick.getCards()));
            try{
            currentThread().sleep(300);
            }
            catch(Exception E){
                System.out.println("insomnia");
            }
            center.takeTrick(person);
            
            if(!hand[1].empty){ // if there are sstill more tricks to be played after this one
                trick=new Trick(people);
            } else {
                done = true;
                doRoundEnd();
            }
        }
        else if(person!=0){
            playCard();
            person=(person+1)%people;
        }
        else{//if person == 0, wait for a response from the button
            try{
            synchronized(lock){
                lock.wait();
            }
            }
            catch(Exception ex){
                System.out.println("Error in proceed synchronization awaiting player response");
            }
            
            ArrayList<Integer> legal = legalMoves();
            for(int i = 0; i<legal.size(); i++){
                if(hand[person].cards[legal.get(i)]==playercard){

                    start = true;
                    if(playercard.suit==3){
                        heartsbroken=true;
                    }
                    
                    trick.playCard(playercard, 0);
                    hand[0].playCard(playercard);
                    center.playCard(playercard,0);

                    person=(person+1)%people;
                    
                }
            }
            
            
        }

    }
    }

public void displayScores(){
    JOptionPane.showMessageDialog(this,ScoreList,"Scores",JOptionPane.PLAIN_MESSAGE);
}
        
    
    
    public boolean hasnonheart(){
        for(int i = 0; i < hand[person].cards.length; i++){
            if(hand[person].cards[i]!=null && hand[person].cards[i].suit!=3){
                return true;
            }
        }
        return false;
    }
    
    public boolean hassuit(int suit){
        for(int i = 0; i < hand[person].cards.length; i++){
            if(hand[person].cards[i]!=null && hand[person].cards[i].suit==suit){
                return true;
            }
        }
        return false;
    }

    
    public ArrayList legalMoves(){
        
        ArrayList<Integer> legal = new ArrayList<>();
        
        if(!start){
            for(int i =0;i<hand[person].cards.length;i++){
                if(hand[person].cards[i].suit==1 && hand[person].cards[i].value==1){
                    legal.add(i);
                }
            }
        } 
        else if(trick.leader==-1){//this means that we are getting the legal plays for a lead
            if(this.heartsbroken){
                for(int i = 0; i < hand[person].cards.length; i++){
                    if(hand[person].cards[i]!=null){
                        legal.add(i);
                    }
                }
            }
            else if(hasnonheart() == false){
                heartsbroken = true;
                for(int i = 0; i < hand[person].cards.length; i++){
                    if(hand[person].cards[i]!=null){
                        legal.add(i);
                    }
                }                             
            }
            else{
                for(int i = 0; i < hand[person].cards.length; i++){
                    if(hand[person].cards[i]!=null && hand[person].cards[i].suit!=3){
                        legal.add(i);
                    }
                }
            }
        }
        else{//this is for a nonlead situation
        int leadsuit = trick.cards[trick.leader].suit;
            if(hassuit(leadsuit)){//player needs to follow suit if they can
                for(int i = 0; i < hand[person].cards.length; i++){
                    if(hand[person].cards[i]!=null && hand[person].cards[i].suit==leadsuit){
                        legal.add(i);
                    }
                } 
            }
            else{//if you can't follow suit, then whatever you want. I dont like the rule where you cant play the Queen on the first trick anyway
                for(int i = 0; i < hand[person].cards.length; i++){
                    if(hand[person].cards[i]!=null){
                        legal.add(i);
                    }
                }
            }
        }
        
        return legal;
        
    }
    
    public int playCard(){
     
        //this block checks various conditions...
        if(!start){
            for(int i =0;i<hand[person].cards.length;i++){
                if(hand[person].cards[i].suit==1 && hand[person].cards[i].value==1){
                    start = true;
                    Card c = hand[person].cards[i];
                    trick.playCard(c, person);
                    hand[person].playCard(i);
                    center.playCard(c, person);
                    return i;
                }
            }
        }
        
        //Easy AI
        ArrayList<Integer> legalMoves = legalMoves();
        Random x = new Random();
        
        int p = legalMoves.get(x.nextInt(legalMoves.size()));//returns a random, but legal, play
        
        Card c = hand[person].cards[p];
        if(c.suit == 3){
            heartsbroken=true;
        }
        trick.playCard(c, person);
        hand[person].playCard(p);
        center.playCard(c, person);
        return p;
    }
    
    public Card[] getPassCards(int player){
        
        //easy AI
        Card [] p = new Card[3];
        Random x = new Random();
        int r = x.nextInt(hand[player].cards.length);
        int count = 0;
        
        while(p[2]==null){
            if(hand[player].cards[r]!=null){
                p[count] = hand[player].cards[r];
                hand[player].cards[r] = null;
                count++;
                r = x.nextInt(hand[player].cards.length);
            }
        }
        return p;
        }
}


