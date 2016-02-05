/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author BeerSmokinGenius
 */
public class Hand extends javax.swing.JPanel{

    /**
     * Creates new form OpponentVerticalHand
     */
    Card[] cards;
    Card invisibleCard;
    char side;
    boolean empty = false;
    
    
    public Hand(Card[] hand,char side) {
        super();
        invisibleCard=new Card(1,1);
        invisibleCard.setBorderPainted(false);
        invisibleCard.setOpaque(false);
        invisibleCard.setVisible(true);
        if(side=='l'||side=='r'){
               invisibleCard.setPreferredSize(new Dimension(120,80)); 
        }
        if(side=='a'||side=='p'){
                invisibleCard.setPreferredSize(new Dimension(80,120)); 
        }
        this.side=side;
        this.cards=hand;
        initComponents();
        if(side!='p'){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.setOpaque(false);
        if(side=='r'){
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx=0.5;
            c.gridx=0;
            c.gridy=0;

            hand[0].setCard('m');
            this.add(hand[0],c);
            for(int i=1;i<hand.length-1;i++){
                c.gridy=i;
                hand[i].setCard('t');
                this.add(hand[i],c);

            }
            c.gridy=hand.length-1;
            hand[hand.length-1].setCard('g');
            this.add(hand[hand.length-1],c);
        }
        else if(side=='l'){
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx=0.5;
            c.gridx=0;
            c.gridy=0;
            
            hand[0].setCard('u');
            this.add(hand[0],c);
            for(int i=1;i<hand.length-1;i++){
                c.gridy=i;
                hand[i].setCard('c');
                this.add(hand[i],c);

            }
            c.gridy=hand.length-1;
            hand[hand.length-1].setCard('j');
            this.add(hand[hand.length-1],c);
        }
        else if(side=='a'){
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty=0.5;
            c.gridx=0;
            c.gridy=0;
            
            hand[0].setCard('z');
            this.add(hand[0],c);
            for(int i=1;i<hand.length-1;i++){
                c.gridx=i;
                hand[i].setCard('o');
                this.add(hand[i],c);

            }
            c.gridx=hand.length-1;
            hand[hand.length-1].setCard('f');
            this.add(hand[hand.length-1],c);
        }
        }
        else{
        FlowLayout flow = new FlowLayout();
        this.setOpaque(false);
        flow.setHgap(0);
        flow.setVgap(0);
        this.setLayout(flow);
        for(int i = 0;i<hand.length;i++){
            hand[i].setCard('p');
            this.add(hand[i]);
        }
        }
        this.setVisible(true);
    }
    public void playCard(int i){
        char disp = cards[i].disp;
        this.remove(cards[i]);
        if(this.getComponentCount()>1){
        if(cards[i].disp=='z'){
            for(int j=i+1; j<cards.length; j++){
                if(cards[j]!=null){
                cards[j].setCard('z');
                j=cards.length;
                }
            }
        }
        if(cards[i].disp=='m'){
            for(int j=i+1; j<cards.length; j++){
                if(cards[j]!=null){
                cards[j].setCard('m');
                j=cards.length;
                }
            }
        }
        if(cards[i].disp=='j'){
            for(int j=i-1; j>=0; j--){
                if(cards[j]!=null){
                cards[j].setCard('j');
                j=-1;
                }
            }
        }
        if(cards[i].disp=='f'){
            for(int j=i-1; j>=0; j--){
                if(cards[j]!=null){
                cards[j].setCard('f');
                j=-1;
                }
            }
        }
        if(cards[i].disp=='u'){
            for(int j=i+1; j<cards.length; j++){
                if(cards[j]!=null){
                cards[j].setCard('u');
                j=cards.length;
                }
            }
        }
        if(cards[i].disp=='m'){
            for(int j=i-1; j>=0; j--){
                if(cards[j]!=null){
                cards[j].setCard('m');
                j=-1;
                }
            }
        }
        if(cards[i].disp=='g'){
            for(int j=i-1; j>=0; j--){
                if(cards[j]!=null){
                cards[j].setCard('g');
                j=-1;
                }
            }
        }
        }
        
        else if(this.getComponentCount()==1){
            Card c = (Card)this.getComponent(0);

            
                    if(c.disp=='j'){
                        c.setCard('l');
                    }
                    if(c.disp=='c'){
                        c.setCard('l');
                    }
                    if(c.disp=='u'){
                        c.setCard('l');
                    }
                    if(c.disp=='z'){
                        c.setCard('a');
                    }
                    if(c.disp=='o'){
                        c.setCard('a');
                    }
                    if(c.disp=='f'){
                        c.setCard('a');
                    }
                    if(c.disp=='g'){
                        c.setCard('r');
                    }
                    if(c.disp=='t'){
                        c.setCard('r');
                    }
                    if(c.disp=='m'){
                        c.setCard('r');
                    }
        }
        else if(this.getComponentCount()==0){
            
            this.add(invisibleCard);
            empty=true;
        }
        cards[i]=null;
        this.revalidate();
        this.repaint();
        
    }
    public void playCard(Card i){
        this.remove(i);
        
        
        for(int j=0;j<cards.length;j++){
            if(i.equals(cards[j])){
                cards[j]=null;
            }
        }
        if(this.getComponentCount()==0){
            
            this.add(invisibleCard);
            empty=true;
        }
        this.revalidate();
        this.repaint();
    }
    
    public int getSelectedCount(){
        int selected = 0;
        for(int i=0; i<cards.length; i++){
            if(cards[i].select==true){
                selected++;
            }
        }
        return selected;
    }
    
        public Card[] getSelectedCards(boolean remove){
        ArrayList<Card> selected = new ArrayList<Card>(3);
        for(int i=0; i<cards.length; i++){
            if(cards[i]!=null&&cards[i].select==true){
                selected.add(cards[i]);
                if(remove){
                cards[i]= null;
                this.remove(cards[i]);
                }
            }
        }
        return selected.toArray(new Card[]{});
    }
    
    public void add1Card(Card cd){//one of the ugliest operations... inserting into an array
        this.removeAll();
        clean();
        boolean added = false;
        Card[] nHand = new Card[cards.length+1];
        int counter = 0;
        while(added == false && counter<cards.length){
            if(cd.suit<cards[counter].suit||(cd.suit<=cards[counter].suit && cd.value<cards[counter].value)){
                added = true;
                nHand[counter]=cd;
            }
            else{
                nHand[counter]=cards[counter];
                counter = counter+1;
            }
        }
        while(added == true && counter<cards.length){
                nHand[counter+1]=cards[counter];
                counter = counter+1;
        }
        if(added==false){
            nHand[counter] = cd;
        }
        
        this.cards = nHand;
        
        if(side=='p'){
        for(int i = 0; i< this.cards.length; i++){
            cards[i].setCard('p');
            this.add(cards[i]);
        }
        }
        else if(side=='r'){
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx=0.5;
            c.gridx=0;
            c.gridy=0;

            cards[0].setCard('m');
            this.add(cards[0],c);
            for(int i=1;i<cards.length-1;i++){
                c.gridy=i;
                cards[i].setCard('t');
                this.add(cards[i],c);

            }
            c.gridy=cards.length-1;
            cards[cards.length-1].setCard('g');
            this.add(cards[cards.length-1],c);
        }
        else if(side=='l'){
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx=0.5;
            c.gridx=0;
            c.gridy=0;
            
            cards[0].setCard('u');
            this.add(cards[0],c);
            for(int i=1;i<cards.length-1;i++){
                c.gridy=i;
                cards[i].setCard('c');
                this.add(cards[i],c);

            }
            c.gridy=cards.length-1;
            cards[cards.length-1].setCard('j');
            this.add(cards[cards.length-1],c);
        }
        else if(side=='a'){
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty=0.5;
            c.gridx=0;
            c.gridy=0;
            
            cards[0].setCard('z');
            this.add(cards[0],c);
            for(int i=1;i<cards.length-1;i++){
                c.gridx=i;
                cards[i].setCard('o');
                this.add(cards[i],c);

            }
            c.gridx=cards.length-1;
            cards[cards.length-1].setCard('f');
            this.add(cards[cards.length-1],c);
        }
    }
    
    private void addCard(Card c){//one of the ugliest operations... inserting into an array
        boolean added = false;
        Card[] nHand = new Card[cards.length+1];
        int counter = 0;
        while(added == false && counter<cards.length){
            if(c.suit<cards[counter].suit||(c.suit<=cards[counter].suit && c.value<cards[counter].value)){
                added = true;
                nHand[counter]=c;
            }
            else{
                nHand[counter]=cards[counter];
                counter = counter+1;
            }
        }
        while(added == true && counter<cards.length){
                nHand[counter+1]=cards[counter];
                counter = counter+1;
        }
        if(added==false){
            nHand[counter] = c;
        }

        this.cards = nHand;
    }
    
    public void addCards(Card[] newcards){
        this.removeAll();
        clean();
        for(int i = 0; i<newcards.length; i++){
            addCard(newcards[i]);
        }
        
        if(side == 'p'){
        for(int i = 0; i< this.cards.length; i++){
            cards[i].setCard('p');
            this.add(cards[i]);
        }
        }
        else if(side=='r'){
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx=0.5;
            c.gridx=0;
            c.gridy=0;

            cards[0].setCard('m');
            this.add(cards[0],c);
            for(int i=1;i<cards.length-1;i++){
                c.gridy=i;
                cards[i].setCard('t');
                this.add(cards[i],c);

            }
            c.gridy=cards.length-1;
            cards[cards.length-1].setCard('g');
            this.add(cards[cards.length-1],c);
        }
        else if(side=='l'){
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx=0.5;
            c.gridx=0;
            c.gridy=0;
            
            cards[0].setCard('u');
            this.add(cards[0],c);
            for(int i=1;i<cards.length-1;i++){
                c.gridy=i;
                cards[i].setCard('c');
                this.add(cards[i],c);

            }
            c.gridy=cards.length-1;
            cards[cards.length-1].setCard('j');
            this.add(cards[cards.length-1],c);
        }
        else if(side=='a'){
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty=0.5;
            c.gridx=0;
            c.gridy=0;
            
            cards[0].setCard('z');
            this.add(cards[0],c);
            for(int i=1;i<cards.length-1;i++){
                c.gridx=i;
                cards[i].setCard('o');
                this.add(cards[i],c);

            }
            c.gridx=cards.length-1;
            cards[cards.length-1].setCard('f');
            this.add(cards[cards.length-1],c);
            this.revalidate();
        }
    }
  
    public void clean(){//removes null cards from the array
        Card [] nHand;
        int count = 0;
        for(int i=0;i<cards.length; i++){
            if(cards[i]!=null){
                count++;
            }
        }
        nHand = new Card[count];
        count = 0;
        for(int i=0;i<cards.length; i++){
            if(cards[i]!=null){
                nHand[count]=cards[i];
                count++;
            }
        }
        cards = nHand;
        
    }
        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
