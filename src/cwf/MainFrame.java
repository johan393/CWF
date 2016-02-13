/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cwf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.Socket;
import javax.swing.Box;
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
    public static JLabel aos;
    public static int game;
    public static int platform;

    GamePanel panel;
    JMenuBar menuBar;
    String[] players;
    Image bg;
    

    int people;
    char type;

    public MainFrame() {
        super();
        
        loadsettings();
        bg = (new ImageIcon(bgtheme)).getImage();
        initComponents();
        

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize);
        this.setLayout(new BorderLayout());

        System.out.println(players);
        
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
            ((JLabel)panel.getScoreList().getComponent(0)).setText("   " + playerInput.getText()+ "   ");
            for(int i=1;i<people;i++){
                players[i]=compInput[i].getText();
                ((JLabel)panel.getScoreList().getComponent(i)).setText("   " + compInput[i].getText()+ "   ");
            }
            writesettings();
        }});

        
        JMenuItem themes = new JMenuItem("Modify Themes");
        themes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JTabbedPane themeselector = new JTabbedPane();
                JPanel bgtab = new JPanel();
                JPanel bgsub = new JPanel();
                bgsub.setLayout(new BorderLayout());
                JButton bgbutton = new JButton("Save This Choice");
                
                bgtab.setLayout(new FlowLayout());
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
                        panel.bg.flush();
                        panel.bg = new ImageIcon("themes\\" + MainFrame.bgtheme + "\\bg.png").getImage();
                        panel.repaint();
                    }
                });
                
                bgsub.add(bglist, BorderLayout.NORTH);
                bgsub.add(bgbutton, BorderLayout.SOUTH);
                samp =  new JLabel(new ImageIcon("themes\\" + bgtheme + "\\sample.png"));
                bgtab.add(bgsub);
                bgtab.add(samp);
                
                ////
                JPanel cftab = new JPanel();

                cftab.setLayout(new GridBagLayout());
                
                JButton cfbutton = new JButton("Save This Choice");
                GridBagConstraints con = new GridBagConstraints();
                
                String[] cfList = new String[]{"basic", "ornamental"};
                JList cflist = new JList(cfList);
                cflist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                cflist.setFixedCellWidth(250);
                cflist.setFixedCellHeight(235/cfList.length);
                cfbutton.setPreferredSize(new Dimension(250,25));
                
                cflist.addListSelectionListener(new ListSelectionListener(){
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if(!cflist.getValueIsAdjusting()){
                            cftab.remove(aos);
                            aos = new JLabel(new ImageIcon(("themes\\" + cflist.getSelectedValue() + "\\1_1.png")));
                            System.out.println(cflist.getSelectedValue());   
                            cftab.add(aos, con);
                            themeselector.revalidate();
                        }
                    }
                });
                cfbutton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        frontTheme = (String) cflist.getSelectedValue();
                        
                        for(int i = 0; i<panel.hand[0].cards.length; i++){
                            if(panel.hand[0].cards[i]!=null){
                                panel.hand[0].cards[i].setCard('p');
                            }
                        }
                        
                        for(int i = 0; i<people; i++){
                            if(panel.center.current[i]!=null){
                                panel.center.current[i].setCard('p');
                            }
                        }
                        panel.repaint();
                    }
                });
                
                

                con.gridx=0;
                con.gridy=0;
                con.gridheight=11;
                con.gridwidth=3;
                
                cftab.add(cflist, con);

                con.gridy=12;
                con.gridheight=1;
                cftab.add(cfbutton, con);
                aos =  new JLabel(new ImageIcon("themes\\" + frontTheme + "\\1_1.png"));
                
                con.insets=new Insets(70,95,50,85);
                con.gridx=6;
                con.gridy=5;

                cftab.add(aos,con);
                ////
                themeselector.addTab("Background", bgtab);
                themeselector.addTab("Cards", cftab);
                JOptionPane.showMessageDialog(panel,themeselector,"Pick Your Theme",JOptionPane.PLAIN_MESSAGE);
                
                writesettings();
                
            }
        });
        
        
        options.add(names);
        options.add(themes);

        JMenuItem leavelobby = new JMenuItem("Leave Game");
        JMenuItem exit = new JMenuItem("Exit App");
        JMenu exitmenu = new JMenu("Exit");
        
        exit.addActionListener(

        new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        System.exit(0);
        }});
        
        

        exitmenu.add(leavelobby);
        exitmenu.add(exit);
        menuBar.add(game);
        menuBar.add(options);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(exitmenu);

        this.setJMenuBar(menuBar);
        
        this.setResizable(false);
        //this.pack();

        this.toFront();
        requestFocus();
        this.setVisible(true);
        if(platform==0){
            panel = new HeartsPanel(people,screenSize, players);
        }
        else{
            Client client = new Client();
            client.setPreferredSize(screenSize);
            this.add(client);
            client.validate();
            client.repaint();
            Socket[] cli = client.connect();
            if(Client.host){
                panel = new HostHeartsPanel(people,screenSize, players[0], cli);
            }
            else{
                panel = new ClientHeartsPanel(people,screenSize, players[0], cli);
            }
            this.remove(client);
        }
        panel.setPreferredSize(screenSize);
        
        this.add(panel);

        

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
            game = Integer.parseInt(in.readLine());
            platform = Integer.parseInt(in.readLine());
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
            out.write(Integer.toString(game));
            out.write(System.lineSeparator());
            out.write(Integer.toString(platform));
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
