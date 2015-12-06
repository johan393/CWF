/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author BeerSmokinGenius
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     *
     */
    public static String theme;
    public static String frontTheme;
    public static String bgtheme;
    public static String buttonColor;
    public static JLabel samp;

    HeartsPanel panel;
    JMenuBar menuBar;
    String[] players;

    int people;
    char type;

    public MainFrame() {
        super();
        loadsettings();
        initComponents();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize);
        this.setLayout(new BorderLayout());


        panel = new HeartsPanel(people,screenSize, players);
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
        platformDescriptor.setEnabled(false);
        JMenuItem scores = new JMenuItem("Current Scores");
        scores.addActionListener(
            new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                panel.displayScores();
            }
        });
        
        //game.addSeparator();
        game.add(scores);
        game.addSeparator();
        game.add(platformDescriptor);
        game.add(net);
        game.add(local);


        JMenuItem names = new JMenuItem("Change Names");
        JMenu options = new JMenu("Options");
        names.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){

            JPanel nameentry = new JPanel();
            GridLayout grid = new GridLayout(people,2);
            nameentry.setLayout(grid);
            JTextField compInput[] = new JTextField[people];

            nameentry.add(new JLabel("Player Name: "));
            JTextField playerInput = new JTextField(8);
            playerInput.setText(players[0]);
            nameentry.add(playerInput);

            for(int i=1;i<people;i++){
                nameentry.add(new JLabel("Computer " + i + ": "));
                compInput[i] = new JTextField(8);
                compInput[i].setText(players[i]);
                nameentry.add(compInput[i]);
            }

            JOptionPane.showMessageDialog(panel,nameentry,"Name Settings",JOptionPane.PLAIN_MESSAGE);

            players[0]=playerInput.getText();
            for(int i=1;i<people;i++){
                players[i]=compInput[i].getText();
            }
            writesettings();
        }});

        
        JMenuItem themes = new JMenuItem("Themes");
        themes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JTabbedPane themeselector = new JTabbedPane();
                JPanel bgtab = new JPanel();
                JPanel bgsub = new JPanel();
                bgsub.setLayout(new BorderLayout());
                JButton bgbutton = new JButton("Save This Choice");
                
                bgtab.setLayout(new FlowLayout());
                bgtab.setBackground(Color.white);
                String[] bgList = new String[]{"blue", "charcoal", "green", "green ice", "ice", "lightning", "marble", "white"};
                JList bglist = new JList(bgList);
                bglist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                bglist.setFixedCellWidth(250);
                bglist.setFixedCellHeight(235/bgList.length);
                
                
                bglist.addListSelectionListener(new ListSelectionListener(){
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if(!bglist.getValueIsAdjusting()){
                            bgtab.remove(samp);
                            samp = new JLabel(new ImageIcon(("themes\\bg-" + bglist.getSelectedValue() + "\\sample.png")));
                            bgtab.add(samp);
                            themeselector.revalidate();
                        }
                    }
                });
                
                bgbutton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        bgtheme = "bg-" + bglist.getSelectedValue();
                        panel.bg = new ImageIcon("themes\\" + MainFrame.bgtheme + "\\bg.png").getImage();
                        panel.repaint();
                    }
                });
                
                bgsub.add(bglist, BorderLayout.NORTH);
                bgsub.add(bgbutton, BorderLayout.SOUTH);
                samp =  new JLabel(new ImageIcon("themes\\" + bgtheme + "\\sample.png"));
                bgtab.add(bgsub);
                bgtab.add(samp);
                themeselector.addTab("Background", bgtab);
                JOptionPane.showMessageDialog(panel,themeselector,"Pick Your Theme",JOptionPane.PLAIN_MESSAGE);
                
                writesettings();
                
            }
        });
        
        
        options.add(names);
        options.add(themes);


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

    public void loadsettings(){
        try{
            FileReader filein = new FileReader("settings.ini");
            BufferedReader in = new BufferedReader(filein);
            people = Integer.parseInt(in.readLine());
            players = new String[people];
            for(int i=0;i<people;i++){
                players[i]=in.readLine();
            }
            theme = in.readLine();
            frontTheme = in.readLine();
            bgtheme = in.readLine();
            buttonColor = in.readLine();
            in.close();
            filein.close();
        }

        catch(Exception e){
            System.out.println("there was an issue reading the settings");
        }

    }

    public void writesettings(){
        try{
            FileWriter fileout = new FileWriter("settings.ini");
            BufferedWriter out = new BufferedWriter(fileout);
            out.write(Integer.toString(people));
            out.write(System.lineSeparator());
            for(int i=0;i<people;i++){
                out.write(players[i]);
                out.write(System.lineSeparator());
            }
            out.write(theme);
            out.write(System.lineSeparator());
            out.write(frontTheme);
            out.write(System.lineSeparator());
            out.write(bgtheme);
            out.write(System.lineSeparator());
            out.write(buttonColor);
            out.write(System.lineSeparator());
            out.close();
            fileout.close();
        }
        catch(Exception e){
            System.out.println("there was an issue writing the settings");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        jScrollPane1.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImages(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 798, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 792, Short.MAX_VALUE)
        );

        setBounds(0, 0, 816, 839);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
