/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.net.URLDecoder;

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
        
        /*try{
       dir = URLDecoder.decode(CWF.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("CWF.jar", ""), "UTF-8");
        }
        catch(Exception e){
            System.out.println("encode error");
        }
        System.out.println(dir);
        */
       MainFrame main = new MainFrame();
       
       main.setVisible(true);
       //main.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
       main.Game();       

    }
    
}
    
