package com.trapShootin.trpshootin;

import javax.swing.*;
import java.awt.*;

class myPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public void paintComponent(Graphics g) {
        TrapShooting.shootGame.repaint(g);
    }

}
