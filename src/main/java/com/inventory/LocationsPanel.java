package com.inventory;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class LocationsPanel extends JLabel {
    public LocationsPanel(ActionListener actionListener, int w, int h, ImageIcon image, Inventory inv) {
        this.setSize(w, h);
        
        // set background image
        this.setIcon(image);
                
        // load buttons
        for (String loc: inv.getLocations()) {
            JButton button = new JButton(loc + ": " + inv.getNumSlots(loc));
            button.setBounds(inv.getBounds(loc));
            button.setName("location|%s".formatted(loc));
            button.addActionListener(actionListener);
            this.add(button);
        }
    }
}