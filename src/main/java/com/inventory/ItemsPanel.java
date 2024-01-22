package com.inventory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.*;
import java.util.EventListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ItemsPanel extends JPanel implements ActionListener {
    EventListener listener;
    Inventory inv;
    JPanel itemsPanel;
    JLabel itemTitle;
    JButton itemPreview;
    JTextField countField;
    JPanel dimPanel;
    JButton addItemButton;
//    private final JPanel itemsPanel = new JPanel(new GridLayout(5, 5, GUI.GAP, GUI.GAP));
//    private final JScrollPane scrollable = new JScrollPane(itemsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//    private final JPanel optionsPanel = new JPanel(null);
//    private final JLabel itemTitle = new JLabel();
//    private final JLabel imagePreview = new JLabel();
//    private final JButton subButton = new JButton("-1");
//    private final JTextField countField = new JTextField();
//    private final JButton addButton = new JButton("+1");
//    private final JPanel dimPanel = new JPanel(new GridLayout(1, 3, 0, GUI.GAP));
//    private final JTextField widthField = new JTextField();
//    private final JTextField heightField = new JTextField();

    public ItemsPanel(EventListener listener, Inventory inv) {
        this.listener = listener;
        this.inv = inv;
        setLayout(new BorderLayout());
        
        addItemButton = new JButton("+");
        addItemButton.addActionListener((ActionListener) listener);
        addItemButton.setActionCommand("addItem");
        addItemButton.setOpaque(false);
        
        itemsPanel = new JPanel(new GridLayout(5, 5, 5, 5));
        JScrollPane scrollable = new JScrollPane(
            itemsPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBackground(Handler.BACKGROUND);
        contentPanel.setPreferredSize(new Dimension(310, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        
        
        itemTitle = new JLabel("Placeholder", SwingConstants.CENTER);
        itemTitle.setPreferredSize(new Dimension(100, 33));
        itemTitle.setForeground(Handler.FOREGROUND);
        
        itemPreview = new JButton("No preview available");
        itemPreview.setActionCommand("setItemImage");
        itemPreview.setPreferredSize(new Dimension(290, 290));
        itemPreview.addActionListener(this);
        itemPreview.setOpaque(false);
        
        JPanel infoPanel = new JPanel(new BorderLayout(0, 5));
        infoPanel.setBackground(Handler.SECONDARY);
        
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(itemTitle, BorderLayout.NORTH);
        infoPanel.add(itemPreview, BorderLayout.CENTER);
        
        
        JButton remButton = new JButton("-1");
        remButton.addActionListener(this);
        remButton.setActionCommand("remCount");
        remButton.setOpaque(false);
        
        countField = new JTextField();
        countField.addActionListener(this);
        countField.setActionCommand("countField");
        
        JButton addButton = new JButton("+1");
        addButton.addActionListener(this);
        addButton.setActionCommand("addCount");
        addButton.setOpaque(false);
        
        
        JPanel countPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        countPanel.setBackground(Handler.THIRD);
        countPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        countPanel.setPreferredSize(new Dimension(0, 66));
        countPanel.add(remButton);
        countPanel.add(countField);
        countPanel.add(addButton);
        
        // dimension
        JTextField widthField = new JTextField();
        widthField.setActionCommand("widthField");
        widthField.addActionListener(this);
        
        JLabel dimLabel = new JLabel("X", SwingConstants.CENTER);
        dimLabel.setOpaque(false);
        
        JTextField heightField = new JTextField();
        heightField.setActionCommand("heightField");
        heightField.addActionListener(this);
        
        dimPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        dimPanel.setBackground(Handler.THIRD);
        dimPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dimPanel.setPreferredSize(new Dimension(0, 66));
        dimPanel.add(widthField);
        dimPanel.add(dimLabel);
        dimPanel.add(heightField);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener((ActionListener) listener);
        backButton.setActionCommand("slots");
        backButton.setOpaque(false);
        
        
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 0, 50));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        controlPanel.setBackground(Handler.SECONDARY);
        controlPanel.add(countPanel);
        controlPanel.add(dimPanel);
        controlPanel.add(backButton);
        
        contentPanel.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(controlPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.EAST);
        add(scrollable);
    }
    
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (!aFlag) return;
        
        itemsPanel.removeAll();
        for (String itemName: inv.getItems()) {
            inv.setItem(itemName);
            JButton button = new JButton();
            button.setText("%s: %d".formatted(itemName, inv.getItemCount()));
            button.setActionCommand("item|%s".formatted(itemName));
            button.addActionListener(this);
            button.setName("item");
            itemsPanel.add(button);
        }
        itemsPanel.add(addItemButton);
        inv.setItem(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        String itemName = inv.getItemName();
        if (itemName == null) { // reset
            itemTitle.setText("Placeholder");
            itemPreview.setText("No preview available");
            countField.setText("0");
            dimPanel.setVisible(false);
        } else {
            itemTitle.setText(itemName);
        
            Image image = new ImageIcon("%s/%s.png".formatted(Handler.FOLDER, itemName)).getImage();
            Image scaled = image.getScaledInstance(itemPreview.getWidth(), itemPreview.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaled);
            itemPreview.setIcon(imageIcon);

            int count = inv.getItemCount();
            countField.setText(Integer.toString(count));
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] actionInfo = e.getActionCommand().split("\\|");
        String action = actionInfo[0];
        
        switch (action) {
            case "setItemImage" -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setAcceptAllFileFilterUsed(false);

                chooser.setDialogTitle("Select an image for the location preview");

                FileNameExtensionFilter restriction = new FileNameExtensionFilter("Select a .png file", "png");
                chooser.addChoosableFileFilter(restriction);

                int r = chooser.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    Path source = new File(chooser.getSelectedFile().getAbsolutePath()).toPath();
                    Path destination = new File("%s/%s/%s.png".formatted(Handler.FOLDER, "Item", inv.getLocationName())).toPath();
                    
                    try {
                        Files.copy(source, destination);
                        setVisible(true); // reload
                    } catch (IOException exception) {
                        itemPreview.setText("An error has occured");
                        System.out.println(exception);
                    }
                }
                    
                    

                    // set the label to the path of the selected file
    //                    l.setText(j.getSelectedFile().getAbsolutePath());
            } case "remCount" -> {
                int count = inv.getItemCount();
                inv.setItemCount(count-1);
            } case "countField" -> {
                String value = ((JTextField) e.getSource()).getText();
                int count = Integer.parseInt(value);
                inv.setItemCount(count);
            } case "addCount" -> {
                int count = inv.getItemCount();
                inv.setItemCount(count+1);
            } case "widthField" -> {
                
            } case "heightField" -> {
                
            } case "item" -> {
                inv.setItem(actionInfo[1]);
                
            }
        }
        repaint();
    }
}