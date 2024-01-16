package com.inventory;

import java.awt.event.ActionListener;

public class ItemsPanel extends Panel {
    public ItemsPanel(ActionListener actionLister, int w, int h) {
        super(actionLister, w, h);
        
        this.add(backButton);
    }
    
}