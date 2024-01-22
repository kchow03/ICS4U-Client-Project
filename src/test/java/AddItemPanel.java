package com.inventory;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.EventListener;

public class AddItemPanel extends Panel {
    private JTextField name = new JTextField();
    private JTextField count = new JTextField();
    private JButton submit = new JButton("Submit");
    
    public AddItemPanel(EventListener listener, int w, int h) {
        super(listener, w, h);
        
        int width = GUI.GAP*6;
        int height = GUI.GAP*3;
        
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setForeground(GUI.textColour);
        JComponent[] nameRow = {nameLabel, name};
        for (int i = 0; i < nameRow.length; i++) {
            nameRow[i].setBounds((width*2+GUI.GAP)/2 + width*i + GUI.GAP*(i+1), GUI.GAP*5, width, height);
            this.add(nameRow[i]);
        }
        
        JLabel countLabel = new JLabel("Count: ");
        countLabel.setForeground(GUI.textColour);
        JComponent[] countRow = {countLabel, count};
        for (int i = 0; i < countRow.length; i++) {
            countRow[i].setBounds((width*2+GUI.GAP)/2 + width*i + GUI.GAP*(i+1), GUI.GAP*10, width, height);
            this.add(countRow[i]);
        }
        
        submit.setBounds(100, 250, width, height);
        submit.setName("submit");
        submit.addActionListener((ActionListener) listener);
        
        
        
        this.add(submit);
        this.add(backButton);
    }
    
    public void reset() {
        name.setText("");
        count.setText("");
    }
    
    public String getItemName() {
        String text = name.getText();
        return text.isEmpty() ? "placeholder" : text;
    }
    
    public int getItemCount() {
        try { 
            return Integer.parseInt(count.getText());
        } catch (Exception e) { // not a number
            return 0;
        }
       
    }
}