/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Field;
import java.util.concurrent.Semaphore;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.Timer;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author BeerSmokinGenius
 */
public class Center extends javax.swing.JPanel{
     
    /**
     * Creates new form Center
     */

    int people;

    
    public JTimer task;//contains the code to be executed by timer
    public Timer t;//animator timer
    public String[] names;
    JLabel[] namepanels;
    int playerpos;
    Card[] current;

    
    public Center(int people, Dimension d, String[] names, int playerpos) {
        super();
        initComponents();
        this.people=people;
        this.setLayout(null);
        
        
        this.setVisible(true);
        t= new Timer();
        this.names = names;
        this.playerpos = playerpos;
        current = new Card[people];
    }
    public void playCard(Card card,int position){
        current[position] = card;
        task = new JTimer(this, people);
        JTimer.x=JTimer.iposx[position];
        JTimer.y=JTimer.iposy[position];
        
        if(this.people==4&&(position==0|position==2)){
            JTimer.dx=(JTimer.posx[position]-JTimer.x)/110;
            JTimer.dy=(JTimer.posy[position]-JTimer.y)/110;
        }
        else{
            JTimer.dx=(JTimer.posx[position]-JTimer.x)/160;
            JTimer.dy=(JTimer.posy[position]-JTimer.y)/160;
        }
        JTimer.animatedCard=Toolkit.getDefaultToolkit().getImage("themes\\" + MainFrame.frontTheme + "\\" + card.loc);
        JTimer.player = position;
        JTimer.direction = 1;
                
        JTimer.acard=card;
        JTimer.acard.setCard('p');
        
        System.out.println("");
        System.out.println("");
        System.out.println(card.suit);
        System.out.println(card.value);
        System.out.println(card.loc);
        System.out.println(position);
               
        
        
        
        t.scheduleAtFixedRate(task, 0, 8);
        try{
            synchronized(task){
                task.wait();
            }
        }
        catch(Exception e){
            System.out.println("Error waiting for Timer to finish animation");
        }
        
    }
    
    public void pos(Dimension d){
        //ox,oy is center of middle panel
        int[] posx=new int[people];
        int[] posy=new int[people];
        int[] iposx=new int[people];
        int[] iposy=new int[people];
        int ox=(int) ((d.getWidth()-240)/2);
        int oy=(int) ((d.getHeight()-240)/2);

        if(people==4){
            posx[0]=ox-40;
            posx[1]=ox-120;
            posx[2]=ox-40;
            posx[3]=ox+40;
            
            posy[0]=oy+60;
            posy[1]=oy-60;
            posy[2]=oy-180;
            posy[3]=oy-60;
            
            iposx[0]=ox-40;
            iposx[1]=0;
            iposx[2]=ox-40;
            iposx[3]=(int) d.getWidth()-320;
            
            iposy[0]=(int) d.getHeight()-385;
            iposy[1]=oy-60;
            iposy[2]=0;
            iposy[3]=oy-60;
            
        }
        JTimer.setpos(posx, iposx, posy, iposy);
        
        if(namepanels==null){//effectively makes this call a one time thing per game
        FontMetrics metrics = getFontMetrics(new Font("Times New Roman", 0, 24));
        int width;
        Color color;
        try{
        Field field = Class.forName("java.awt.Color").getField(MainFrame.buttonColor);
        color = (Color)field.get(null);
        }
        catch(Exception e){
        color = null;
        }
        namepanels = new JLabel[people];

        if(people == 4){
            for(int i = 0; i<people; i++){
            namepanels[(i+(4-playerpos))%4] = new JLabel(names[i]);
            namepanels[(i+(4-playerpos))%4].setForeground(color);
            }
            namepanels[0].setBounds(JTimer.iposx[0]-480, JTimer.iposy[0]+80, 180, 50);
            namepanels[0].setFont(new Font("Times New Roman", 0 , 24));
            namepanels[1].setBounds(JTimer.iposx[1], JTimer.iposy[1]-245, 180, 50);
            namepanels[1].setFont(new Font("Times New Roman", 0 , 24));
            width = metrics.stringWidth(names[2]);//d
            if(width>180){
                width = 180;
            }
            namepanels[2].setBounds(JTimer.iposx[2]+320-width, JTimer.iposy[2]-10, 180, 50);
            namepanels[2].setFont(new Font("Times New Roman", 0 , 24));
            width = metrics.stringWidth(names[3]);
            if(width>180){
                width = 180;
            }
            namepanels[3].setBounds(JTimer.iposx[3]+ 75-width, JTimer.iposy[3]+290, 180, 50);
            namepanels[3].setFont(new Font("Times New Roman", 0 , 24));
            this.add(namepanels[0]);
            this.add(namepanels[1]);
            this.add(namepanels[2]);
            this.add(namepanels[3]);
        }
        }
    }
    public void takeTrick(int position){
        task = new JTimer(this, people);
        JTimer.x=JTimer.posx[position];
        JTimer.y=JTimer.posy[position];
        JTimer.animatedCard=Toolkit.getDefaultToolkit().getImage("themes\\" + MainFrame.theme + "\\base.png");
        
        if(this.people==4&&(position==0|position==2)){
            JTimer.dx=(JTimer.iposx[position]-JTimer.x)/110;
            JTimer.dy=(JTimer.iposy[position]-JTimer.y)/110;
        }
        else{
            JTimer.dx=(JTimer.iposx[position]-JTimer.x)/160;
            JTimer.dy=(JTimer.iposy[position]-JTimer.y)/160;
        }
        //this.removeAll();
        for(int i = 0; i<current.length; i++){
            this.remove(current[i]);
        }
        JTimer.direction= 0;
        JTimer.player = position;
        t.scheduleAtFixedRate(task, 0, 8);
        try{
            synchronized(task){
                task.wait();
            }
        }
        catch(Exception e){
            System.out.println("Error waiting for Timer to finish animation of taking a trick");
        }

    }
        
    @Override
    public void paint(Graphics g){
        super.paint(g);
        if(JTimer.animatedCard!=null){
            g.drawImage(JTimer.animatedCard, (int)JTimer.x, (int)JTimer.y, 80,120, null);//type cast here is literally just a simpler rounding function
        }
        Toolkit.getDefaultToolkit().sync();
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(480, 400));
        setMinimumSize(new java.awt.Dimension(480, 400));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(480, 400));
        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


}
