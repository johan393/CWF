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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JLabel;

/**
 *
 * @author BeerSmokinGenius
 */
public class HeartsPanel extends javax.swing.JPanel implements ActionListener {

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
    
    //these variables keep track of AI data
    boolean start;
    boolean heartsbroken;
    boolean qplayed; 
    
    public HeartsPanel(int people, Dimension d, String[] players) {
       // super();
        initComponents();
        this.d = d;
        roundcount = 0;
        setLayout(new java.awt.BorderLayout());
        this.people=people;
        hand = new Hand[people]; 
        center = new Center(people, d);
        bg=new ImageIcon("bg_green.png").getImage();
        piles=new ArrayList[people];
        pts=new int[people];
        for(int i=0;i<people;i++){
            piles[i]=new ArrayList<>();
        }
        this.add(center);
        this.setVisible(true);
        
     
        
        t=new Timer(500,this);
        this.players = players;
        
        ScoreList=new JPanel();
        ScoreList.setLayout(new GridBagLayout());
        c=new GridBagConstraints();
        c.gridy=0;
        for(int i=0; i < players.length; i++){
            c.gridx=i;
            ScoreList.add(new JLabel(players[i]), c);
        }
        
        
        
    }
   public void newRound(){
        deck=new Deck();
        deck.shuffle();
        deck.empVal(0,13);
        
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
        person = getFirstPlayer();///the person to lead the two of clubs
        trick=new Trick(people);
        
        for(int i=0;i<people;i++){
            piles[i].clear();
        }
        
        revalidate();
        center.pos(d);
        t.start();
    }
   public void paintComponent(Graphics g){
     super.paintComponent(g);
     g.drawImage(bg,0,0,null);

   }

   public void setCardListeners(){
       ActionListener e = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if(person==0&&!center.s.isRunning()&&!center.t.isRunning()&&!t.isRunning()){//should not play while someone else is playing a card
            Card c = (Card)e.getSource();
            ArrayList<Integer> legal = legalMoves();
            for(int i = 0; i<legal.size(); i++){
                if(hand[person].cards[legal.get(i)]==c){
                    start = true;
                    if(c.suit==3){
                        heartsbroken=true;
                    }
                    center.playCard(c,0);
                    trick.playCard(c, 0);
                    hand[0].playCard(c);

                    person=(person+1)%people;
                    t.start();                    
                }
            }

            }
        };
       };
       for(int i=0;i<hand[0].cards.length;i++){
           hand[0].cards[i].addActionListener(e);
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
     t.stop();
     roundcount++;
     c.gridy=roundcount;
     for(int i=0;i<people;i++){
         this.remove(hand[i]);
         pts[i]=pts[i]+countPoints(piles[i]);
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
 
 
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(!center.s.isRunning()&&!center.t.isRunning()){//waits for all other animations to complete before initiating another play
        if(trick.trickStatus()==people){//if trick is complete
            System.out.println("everybody played at this point");
            person = trick.highestPlayer(-1);
            piles[person].addAll(Arrays.asList(trick.getCards()));
            center.takeTrick(person);
            if(!hand[1].empty){
                trick=new Trick(people);
            } else {
                doRoundEnd();
            }
            
            
        }
        else if(person!=0){
            int cardtoplay= playCard();
            if(hand[person].cards[cardtoplay].suit==3){
                heartsbroken = true;
            }
            Card c = hand[person].cards[cardtoplay];
            hand[person].playCard(cardtoplay);
            center.playCard(c, person);
            trick.playCard(c, person);
            
            
            person=(person+1)%people;
        }
        else if(person==0){
            t.stop();
        }
    }
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
        
        System.out.println(trick.leader);
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
                    return i;
                }
            }
        }
        
        //Easy AI
        ArrayList<Integer> legalMoves = legalMoves();
        Random x = new Random();
        return legalMoves.get(x.nextInt(legalMoves.size()));//returns a random, but legal, play
    }
}

