/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.Image;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 *
 * @author BeerSmokinGenius
 */
public class JTimer extends TimerTask{
    static Semaphore sem = new Semaphore(1);
    public static int direction;
    public static int player;
    public static Image animatedCard;
    
    public static float x;
    public static float y;
    public static float dx;
    public static float dy;
    public static Card acard;
    
    int people;
    public static int posx[];// center positions by player
    public static int posy[];
    public static int iposx[];//player positions
    public static int iposy[];
    Center center;
    public JTimer(Center center, int people){
        super();
        this.center = center;
        this.people = people;
    }
    @Override
    public void run(){
        if(direction==0){//0 is from center to piles
            if(Math.abs((x)-iposx[player])<5&&Math.abs((y)-iposy[player])<5){
                synchronized(this){
                    this.cancel();
                    x=iposx[player];
                    y=iposy[player];
                    animatedCard=null;
                    center.repaint();
                    this.notify();
                }
            }
            else{
                x=x+dx;
                y=y+dy;
                center.repaint();
            }
        }
        
        else if(direction==1){// 1 is from hand to center
            if(Math.abs((x)-posx[player])<5&&Math.abs((y)-posy[player])<5){
                synchronized(this){
                    
                    this.cancel();
                    x=posx[player];
                    y=posy[player];
                    acard.setBounds((int)x,(int)y,80,120);
                    center.add(acard);
                    animatedCard=null;
                    center.repaint();
                    this.notify();
                }
            }
            else{
                x=x+dx;
                y=y+dy;
                center.repaint();
            }       
        }
        
        
    }
    public static void setpos(int posx[], int iposx[], int posy[], int iposy[]){
        JTimer.posx = posx;
        JTimer.posy = posy;
        JTimer.iposx = iposx;
        JTimer.iposy = iposy;
    }
    public void playcard(){
        
    }
}
