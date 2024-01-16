package com.inventory;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionListener;


public class GridButton extends JPanel {
    public final JButton button;
    
    public GridButton(ActionListener actionListener, String name, String text, int size) {   
        this.setLayout(new BorderLayout());
        this.setOpaque(true);
        
        button = new JButton(text);
        button.setPreferredSize(new Dimension(size, size));
        button.setName(name);
        button.addActionListener(actionListener);
        this.add(button);
    }
}