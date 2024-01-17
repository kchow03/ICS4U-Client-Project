package com.inventory;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class BackButton extends JButton {
    public BackButton(EventListener listener) {
        this.setText("BACK");
        this.setName("back");
        this.setBounds(GUI.GAP, GUI.GAP, GUI.GAP*4, GUI.GAP*2);
        this.addActionListener((ActionListener) listener);
        
        
        // colours
        this.setForeground(Color.decode("#5c5845"));
//        this.setOpaque(true);
//        this.setBackground(Color.decode("#5c5845"));
//        this.setForeground(Color.decode("#f4f0f1"));
    }
}