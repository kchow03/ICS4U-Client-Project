package com.inventory;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.EventListener;


public class GridButton extends JPanel {
    private final JButton button;
    
    public GridButton(EventListener listener, String name, String text) {   
        this.setLayout(new BorderLayout());
        this.setOpaque(true);
        
        button = new JButton(text);
        button.setName(name);
        button.addActionListener((ActionListener) listener);
        this.add(button);
    }
    
    public void setButtonSize(int size) {
        Dimension d = new Dimension(size, size);
        button.setPreferredSize(d);
    }
    
    public String getButtonActionCommand() {
        return button.getName();
    }
}