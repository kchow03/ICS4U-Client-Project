package com.inventory;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.EventListener;


public class ItemsPanel extends Panel {
    private final JPanel itemsPanel = new JPanel(new GridLayout(5, 5, GUI.GAP, GUI.GAP));
    private final JScrollPane scrollable = new JScrollPane(itemsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private final JPanel optionsPanel = new JPanel(null);
    private final JLabel itemTitle = new JLabel();
    private final JLabel imagePreview = new JLabel();
    private final JButton subButton = new JButton("-1");
    private final JTextField countField = new JTextField();
    private final JButton addButton = new JButton("+1");
    private final JPanel dimPanel = new JPanel(new GridLayout(1, 3, 0, GUI.GAP));
    private final JTextField widthField = new JTextField();
    private final JTextField heightField = new JTextField();
    
    public ItemsPanel(EventListener listener, int w, int h) {
        super(listener, w, h);
        
        optionsPanel.setBounds(WIDTH/5*3, 0, WIDTH/5*2, HEIGHT);
        optionsPanel.setBackground(GUI.backgroundColour);
        itemsPanel.setBackground(GUI.backgroundColour);
        scrollable.setBounds(0, 0, WIDTH/5*3, HEIGHT);
        
        int width = optionsPanel.getWidth()-GUI.GAP*2;
        itemTitle.setBounds(GUI.GAP, GUI.GAP, width, GUI.GAP*3);
        itemTitle.setBackground(GUI.secondaryColour);
        itemTitle.setForeground(GUI.textColour);
        
        imagePreview.setBounds(GUI.GAP, GUI.GAP*5, width, width);
        
        subButton.setName("subItemCount");
        countField.setName("itemCount");
        addButton.setName("addItemCount");
        
        int itemWidth = (width - GUI.GAP*2)/3;
        
        JComponent[] controls = {subButton, countField, addButton};
        for (int i = 0; i < controls.length; i++) {
            controls[i].setBounds(GUI.GAP*(i+1)+itemWidth*i, width+GUI.GAP*7, itemWidth, GUI.GAP*3);
            
            if (controls[i] instanceof JButton) {
                ((JButton) controls[i]).addActionListener((ActionListener) listener);
            } else if (controls[i] instanceof JTextField) {
                ((JTextField) controls[i]).addActionListener((ActionListener) listener);
            }
            
            optionsPanel.add(controls[i]);
        }
        
        dimPanel.setVisible(false);
        dimPanel.setBounds(GUI.GAP, width+GUI.GAP*15, width, GUI.GAP*5);
        dimPanel.setOpaque(false);
        JLabel dimLabel = new JLabel("X", SwingConstants.CENTER);
        dimLabel.setForeground(Color.WHITE);
        JComponent[] dimensions = {widthField, dimLabel, heightField};
        for (int i = 0; i < dimensions.length; i++) {
            if (dimensions[i] instanceof JButton) {
                ((JButton) dimensions[i]).addActionListener((ActionListener) listener);
            }
            dimPanel.add(dimensions[i]);
        }
        
        widthField.setName("setWidth");
        heightField.setName("setHeight");
        
        optionsPanel.add(dimPanel);
        optionsPanel.add(itemTitle);
        optionsPanel.add(imagePreview);
        optionsPanel.add(backButton);
        this.add(optionsPanel);
        this.add(scrollable);
    }
    
    public void refresh(EventListener listener, Inventory inv, String loc, int index) {
        int size = ((WIDTH/5*2)-GUI.GAP*4)/5;
        
        itemsPanel.removeAll();
        for (String itemName: inv.getItems(loc, index)) {
            String name = "%s: %d".formatted(itemName, inv.getItemCount(loc, index, itemName));
            GridButton button = new GridButton(listener, "item|%s".formatted(itemName), name);
            button.setButtonSize(size);
            itemsPanel.add(button);
        }
        
        GridButton addItemButton = new GridButton(listener, "addItem|null", "+"); // null is placeholder
        addItemButton.setButtonSize(size);
        itemsPanel.add(addItemButton);
    }
    
    public void update(String itemName, int count) {
        itemTitle.setText(itemName);
        
        ImageIcon icon = new ImageIcon("%s/%s.png".formatted(GUI.FOLDER, itemName));
        ImageIcon scaled = new ImageIcon(icon.getImage().getScaledInstance(imagePreview.getWidth(), imagePreview.getWidth(), Image.SCALE_SMOOTH));
        imagePreview.setIcon(scaled);
        
        countField.setText(Integer.toString(count));
    }
}