package com.inventory;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class ItemPanel extends JPanel implements ActionListener {
    private final Inventory inv;
    
    public ItemPanel(Rectangle r, Inventory inv) {
        this.inv = inv;
        this.setLayout(null);
        this.setBounds(r);
        this.setBackground(Color.GRAY);
    }
    
    public void update(String loc, int index, String item) {
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
