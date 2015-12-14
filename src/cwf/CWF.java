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
public class CWF {

    /**
     * @param args the command line arguments
     */
    
    public static String dir;
    
    public static void main(String[] args) {
        
       dir = CWF.class.getProtectionDomain().getCodeSource().getLocation().getPath();

       MainFrame main = new MainFrame();
       
       main.setVisible(true);
       //main.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
       main.Game();       

    }
    
}
    
