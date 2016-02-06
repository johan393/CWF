/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.util.Random;

/**
 *
 * @author BeerSmokinGenius
 */
public class Deck {
    
    Card[] deck;
    
    public Deck(int max, int suits){
        deck = new Card[max*suits];
        int count = 0;
        for (int i = 0; i<max; i++){
            for(int j=0;j<suits;j++){
                deck[count]=new Card(i,j);
                count++;
            }
        }
    }
    public Deck(){
        deck = new Card[52];
        int count = 0;
        for (int i = 0; i<13; i++){
            for(int j=0;j<4;j++){
                deck[count]=new Card(i,j);
                count++;
            }
        }
    }
    
    public void shuffle(){
        Random rand = new Random();
       
        Card a;
        
        
        for(int i=0;i<150;i++){
            int x = rand.nextInt(52);
            int y = rand.nextInt(52);
            
            a = deck[x];
            deck[x]=deck[y];
            deck[y] = a;
            
        }
        
    }
    Card[][] stddeal(int people){
        int handsize = deck.length/people;
        Card[][] deal = new Card[people][handsize];
        int count =0;

        for(int i =0; i<people;i++){
            for(int j=0;j<handsize;j++){
                deal[i][j] = deck[count];
                count++;
            }
        }
        Card[][] o = organize(deal, people);
        return o;
    }
    
    public Card get(int i){
        return deck[i];
    }
    
    
    public Card[][] organize(Card[][] disorganized, int people){
        int count = 0;
        for(int i = 0; i< people; i++){
        count = 0;
        while(count+1<disorganized[i].length){
            if((disorganized[i][count].suit>disorganized[i][count+1].suit)){
               disorganized = swapCard(i,count,count+1,disorganized);
               count=0;
            }
            else{
                count=count+1;
            }
        }
        count=0;
        while(count+1<disorganized[i].length){
            if((disorganized[i][count].value>disorganized[i][count+1].value)&&(disorganized[i][count].suit==disorganized[i][count+1].suit)){
               disorganized = swapCard(i,count,count+1,disorganized);
               count=0;
            }
            else{
                count=count+1;
            }
        }
        }
        return disorganized;
    }
        
    
    public Card[][] swapCard(int i, int x, int y, Card[][] disorganized){
        Card a;
        a = disorganized[i][x];
        disorganized[i][x]=disorganized[i][y];
        disorganized[i][y] = a;
        return disorganized;
    }
    
    public void empVal(int val, int newval){ //emphasizes a specific value. e.g aces (1) should be 13 because aces are high
        for(int i=0;i<deck.length;i++){
            if(deck[i].value==val){
                deck[i].value=newval;
            }
        }
    }
    
        
    }
    

