package com.inventory;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.EventListener;


public class ToolsPanel extends JFrame {
    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    
    public ToolsPanel(EventListener listener) {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setLayout(null);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, GUI.GAP, GUI.GAP));
        buttonsPanel.setBounds(GUI.GAP, GUI.GAP, WIDTH-GUI.GAP*3, HEIGHT-GUI.GAP*4);
        
        JButton addButton = new JButton("Add location");
        JButton removeButton = new JButton("Remove location");
        
        addButton.setName("addLocation");
        removeButton.setName("remLocation");
        
        addButton.addActionListener((ActionListener) listener);
        removeButton.addActionListener((ActionListener) listener);
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        
        this.add(buttonsPanel);
        this.setVisible(false);
    }
}
