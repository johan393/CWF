/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

/**
 *
 * @author BeerSmokinGenius
 */
public class Trick {
    
    Card[] cards;
    int people;
    int lead;
    int count;
    
    public Trick(int people){
        cards = new Card[people];
        this.people=people;
        count=0;
        lead=-1;
    }
    
    public Card[] getCards(){
        return cards; 
    }
    
    public int trickStatus(){//returns the number people who played on this trick
        return count;
    }
    
    public int highestPlayer(int trump){
      int highestPlayer = lead;
      
      if(trump==-1){
        int suit = cards[lead].suit;
        
        for(int i=0;i<people;i++){
            if(cards[i].suit==suit&&cards[i].value>cards[highestPlayer].value){
                highestPlayer=i;
            }
        }
        
      }
        return highestPlayer;
    }
    
    public void playCard(Card card, int i){

        if(count==0){//lead
            lead=i;
        }
        this.cards[i]=card;
        count=count+1;
    }
    
}
