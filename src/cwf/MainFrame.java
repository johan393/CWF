/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author BeerSmokinGenius
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     * 
     */
    HeartsPanel panel;
    JMenuBar menuBar;
    public MainFrame() {
        super();
        initComponents();
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize);
        this.setLayout(new BorderLayout());
        
        String[] players = {" Me ", " Oak ", " Shan ", " Jim "};
        panel = new HeartsPanel(4,screenSize, players);
        panel.setPreferredSize(screenSize);
        menuBar = new JMenuBar();
        
        JMenu game = new JMenu("Game");
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem net = new JRadioButtonMenuItem("    Online Game");
        JRadioButtonMenuItem local = new JRadioButtonMenuItem("    Versus Computer");
        local.setSelected(true);
        group.add(net);
        group.add(local);
        JMenuItem platformDescriptor = new JMenuItem("Where to Play :");
        game.add(platformDescriptor);
        game.add(net);
        game.add(local);
        
        JMenuItem names = new JMenuItem("Change Names");
        JMenu options = new JMenu("Options");
        names.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
            JPanel nameentry = new JPanel();
            GridLayout grid = new GridLayout(2,5);
            
            
            
            JOptionPane.showMessageDialog(panel,nameentry,null,0);
        }});
        
        options.add(names);
        
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(
        new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        System.exit(0);
        }});
        
        menuBar.add(game);
        menuBar.add(options);
        menuBar.add(exit);
        
        this.setJMenuBar(menuBar);
        this.add(panel);
        
        this.setResizable(false);
        //this.pack();
        
        this.toFront();
        requestFocus();
        this.setVisible(true);
        
    }

    public void Game(){
        panel.newRound();
        revalidate();
        repaint();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImages(null);
        setMaximumSize(new java.awt.Dimension(4000, 4000));
        setPreferredSize(new java.awt.Dimension(1020, 1020));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );

        setBounds(0, 0, 816, 839);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
