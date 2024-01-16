package com.inventory;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public abstract class Panel extends JPanel {
    public final int WIDTH;
    public final int HEIGHT;
    public final BackButton backButton;
    
    
    public Panel(ActionListener actionListener, int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(null);
        this.setBackground(Color.decode("#130e0f"));
        
        backButton = new BackButton(actionListener);
        
        this.setVisible(false);
    }    
}