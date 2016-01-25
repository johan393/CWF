package cwf;

import java.awt.Image;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Michael
 */
public abstract class GamePanel extends JPanel {
    public abstract void newRound();
    public abstract void displayScores();
    public abstract JPanel getScoreList();
    public Image bg;
    public Hand[] hand;
    public Trick trick;
}
