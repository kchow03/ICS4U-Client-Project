package com.inventory;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;


public class AddItemPanel extends JPanel implements ActionListener {
    EventListener listener;
    Inventory inv;
    JPanel itemsPanel;
    JTextField nameField;
    JTextField countField;
    JButton backButton;

    public AddItemPanel(EventListener listener, Inventory inv) {
        this.listener = listener;
        this.inv = inv;
        setLayout(new BorderLayout());
        
        JLabel nameLabel = new JLabel("Name: ", SwingConstants.CENTER);
        nameLabel.setForeground(Handler.FOREGROUND);
        nameField = new JTextField();
        
        JPanel namePanel = new JPanel(new GridLayout(1, 2));
        namePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        namePanel.setBackground(Handler.THIRD);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        
        JLabel countLabel = new JLabel("Count: ", SwingConstants.CENTER);
        countLabel.setForeground(Handler.FOREGROUND);
        countField = new JTextField();
        
        JPanel countPanel = new JPanel(new GridLayout(1, 2));
        countPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        countPanel.setBackground(Handler.THIRD);
        countPanel.add(countLabel);
        countPanel.add(countField);
        
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);
        confirmButton.setActionCommand("confirmButton");
        confirmButton.setOpaque(false);
        
        JPanel controlsPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        controlsPanel.setBackground(Handler.SECONDARY);
        controlsPanel.setPreferredSize(new Dimension(200, 200));
        controlsPanel.add(namePanel);
        controlsPanel.add(countPanel);
        controlsPanel.add(confirmButton);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Handler.BACKGROUND);
        centerPanel.add(controlsPanel);
        
        JLabel label = new JLabel("Items", SwingConstants.CENTER);
        label.setForeground(Handler.FOREGROUND);
        label.setPreferredSize(new Dimension(0, 66));
        
        itemsPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        itemsPanel.setBackground(Handler.SECONDARY);
        JScrollPane scrollable = new JScrollPane(
            itemsPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        
        backButton = new JButton("Back");
        backButton.addActionListener((ActionListener) listener);
        backButton.setOpaque(false);
        backButton.setPreferredSize(new Dimension(0, 50));
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Handler.BACKGROUND);
        contentPanel.setPreferredSize(new Dimension(310, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.add(label, BorderLayout.NORTH);
        contentPanel.add(scrollable, BorderLayout.CENTER);
        contentPanel.add(backButton, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.EAST);
    }
    
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (!aFlag) return;
        backButton.setActionCommand("items|"+inv.getSlotIndex());
        itemsPanel.removeAll();
        for (String itemName: inv.getItemsList()) {
            JButton button = new JButton(itemName);
            button.setOpaque(false);
            button.setActionCommand("item|"+itemName);
            button.addActionListener(this);
            itemsPanel.add(button);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] actionInfo = e.getActionCommand().split("\\|");
        String action = actionInfo[0];
        
        switch (action) {
            case "confirmButton" -> {
                String name = nameField.getText();
                
                if (name.isBlank()) {
                    nameField.setText("Invalid name");
                    return;
                }
                
                try {
                    int count = Integer.parseInt(countField.getText());
                    
                    if (!inv.hasItem(name)) {
                        if (count > 0) {
                            inv.setItem(name);
                            inv.addItem(count);
                            backButton.doClick();

                            if (!inv.hasItemList(name)) {
                                inv.setItemList(name);
                            }
                        } else {
                            countField.setText("Invalid amount");
                        }
                    } else {
                        nameField.setText("Already exists");
                    }
                } catch (Exception error) {
                    countField.setText("Invalid amount");
                }
            } case "item" -> {
                nameField.setText(actionInfo[1]);
            }
        }
    }
}