/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
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
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author BeerSmokinGenius
 */
public class HeartsPanel extends javax.swing.JPanel implements ActionListener {

    /**
     * Creates new form HeartsPanel
     */
    
    Hand [] hand;
    Deck deck;
    int people;
    Center center;
    Image bg;
    
    Timer r;
    Timer t;
    Timer s;
    
    
    ArrayList<Card>[] piles;
    int[] pts;
    Trick trick;
    int person;
    

    
    public HeartsPanel(int people, Dimension d) {
       // super();
        initComponents();
        setLayout(new java.awt.BorderLayout());
        this.people=people;
        hand = new Hand[people]; 
        center = new Center(people, d);
        bg=new ImageIcon("bg_green.png").getImage();
        piles=new ArrayList[people];
        pts=new int[people];
        for(int i=0;i<people;i++){
            piles[i]=new ArrayList<Card>();
        }
        this.add(center);
        this.setVisible(true);
        System.out.println("dd");
        System.out.println(center.getWidth());
        
        s=new Timer(600,new ActionListener() {
        public void actionPerformed(ActionEvent e) { 
            
        }});
        
        r=new Timer(500,new ActionListener() {
        public void actionPerformed(ActionEvent e) { 
 
        }});

        t=new Timer(800,this);
        
        
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
        person = getFirstPlayer();///the first sequence of plays
        trick=new Trick(people);
        revalidate();
        center.pos(people);
        t.start();
    }
   public void paintComponent(Graphics g){
     super.paintComponent(g);
     g.drawImage(bg,0,0,null);

   }

   public void setCardListeners(){
       ActionListener e = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if(person==0&&!center.s.isRunning()){
                t.stop();
            Card c = (Card)e.getSource();
            center.playCard(c,0);
            trick.playCard(c, 0);
            hand[0].playCard(c);
            
            person=(person+1)%people;
            t.start();
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
 public int playCard(int i){
        Random q = new Random();////////////////// piking a card to play. this part will change
        int p = q.nextInt(hand[i].cards.length);
        while(hand[i].cards[p]==null){
            p=q.nextInt(hand[i].cards.length);
        }/////////////////////////////////////////
        
        return p;
        
        
    }
 
 public boolean getRoundEnd(){
     
     int x=0;
        
    for(int i=0;i<people;i++){
        x=x+hand[i].getComponentCount();
    }
        return x==0;
        
 }
 
 public void doRoundEnd(){
     s.stop();
     t.stop();
     String scores="";
    // center.clear();
     for(int i=0;i<people;i++){
         this.remove(hand[i]);
         pts[i]=pts[i]+countPoints(piles[i]);
         scores=scores+Integer.toString(pts[i])+"     ";
     }
     
     System.out.println(scores);
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
        
        if(!center.s.isRunning()){
        if(trick.trickStatus()==people){
            System.out.println("here");
            person = trick.highestPlayer(-1);
            piles[person].addAll(Arrays.asList(trick.getCards()));
            center.takeTrick(person);
            trick=new Trick(people);
            
        }
        else if(person!=0){
            int cardtoplay= playCard(person);
            Card c = hand[person].cards[cardtoplay];
            hand[person].playCard(cardtoplay);
            center.playCard(c, person);
            trick.playCard(c, person);
            
            
            person=(person+1)%people;
        }
    }
        }
    
}