package com.inventory;

import java.awt.Color;
import java.util.EventListener;
import javax.swing.JPanel;

public abstract class Panel extends JPanel {
    public final int WIDTH;
    public final int HEIGHT;
    public final BackButton backButton;
    
    
    public Panel(EventListener listener, int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(null);
        this.setBackground(GUI.backgroundColour);
        
        backButton = new BackButton(listener);
        
        this.setVisible(false);
    }
}