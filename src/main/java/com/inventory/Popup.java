package com.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.Border;

public class Popup extends JFrame implements ActionListener {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 480;
    private static final int GAP = 15;
    private final Inventory inv;
    private final JPanel panel;
    private final JScrollPane scrollable;
    private final ItemPanel itemPanel;
    
    public Popup(Inventory inv) {
        this.inv = inv;
        this.setResizable(false);
        this.setBounds(0, 0, WIDTH, HEIGHT);
//        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLayout(null);
        
        panel = new JPanel(new GridLayout(0, 5, GAP/2, GAP/2));
        scrollable = new JScrollPane(
            panel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollable.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(scrollable);
                
        itemPanel = new ItemPanel(WIDTH, HEIGHT, GAP, inv);
        this.add(itemPanel);
    }
    
    public void slotsMenu(String loc) {
        String title = loc;
        this.setTitle(title);
        
        // reset
        itemPanel.setVisible(false);
        scrollable.setBounds(0, 0, WIDTH, HEIGHT);
        
        
//        this.getContentPane().setBackground(Color.GRAY);
        
        panel.removeAll();
        for (int i = 0; i < inv.getNumSlots(loc); i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setName("slot|" + i);
            button.addActionListener(this);
            panel.add(button);
        }
        panel.revalidate();
        panel.repaint();

        this.setVisible(true);
    }
    
    private void itemsMenu(String loc, int index) {
        String title = loc + " | " + index;
        this.setTitle(title);
        
        panel.removeAll();
        for (String item: inv.getItems(loc, index)) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.red);
            
            JButton button = new JButton(item);
            button.setLayout(new GridLayout(1, 2, 5, 5));
            
            ImageIcon image = new ImageIcon("images/"+item+".png");
            JLabel imageLabel = new JLabel(image);
//            imageLabel.setPreferredSize(new Dimension(50, 50));
            button.add(imageLabel);
            
            button.setBackground(Color.BLACK);
            button.setPreferredSize(new Dimension(76, 76));
            
            button.setName("item|" + item);
            button.addActionListener(this);
            
            buttonPanel.add(button);
            panel.add(buttonPanel);
        }
        panel.revalidate();
        panel.repaint();
    }
    
    private void itemMenu(String loc, int index, String item) {
        String title = String.format("%s | %d | %s", loc, index, item);
        this.setTitle(title);
        
        scrollable.setBounds(0, 0, HEIGHT, HEIGHT);
//        panel.setPreferredSize(new Dimension(HEIGHT, HEIGHT));
        
        
        itemPanel.update(loc, index, item);    
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] info = ((JButton) e.getSource()).getName().split("\\|");
        String type = info[0];
        String name = info[1];
        String[] title = this.getTitle().split(" \\| ");
        
        switch (type) {
            case "slot":
                itemsMenu(title[0], Integer.parseInt(name));
                break;
            case "item":
                itemMenu(title[0], Integer.parseInt(title[1]), name);
                break;
        }
    }
}

class ItemPanel extends JPanel implements ActionListener {
    private final Inventory inv;
    private final int WIDTH;
    private final int HEIGHT;
    private final int GAP;
    private final JLabel itemTitle;
    private final JLabel imageLabel;
    private final JButton minusButton;
    private final JButton addButton;
    private final JTextField countField;
    private final JPanel dimensionPanel;
    private final JTextField widthField;
    private final JTextField heightField;
    
    public ItemPanel(int width, int height, int gap, Inventory inv) {
        this.inv = inv;
        this.WIDTH = width-height;
        this.HEIGHT = height;
        this.GAP = gap;
        
        this.setBounds(HEIGHT, 0, WIDTH, HEIGHT);
        this.setBackground(Color.GRAY);
        this.setLayout(null);
        
        // init
        itemTitle = new JLabel("Placeholder");
        imageLabel = new JLabel();
        
        JPanel countPanel = new JPanel(new GridLayout(1, 3, GAP, 0));
        minusButton = new JButton("-1");
        addButton = new JButton("+1");
        countField = new JTextField();
        
        dimensionPanel = new JPanel(new GridLayout(1, 3, GAP, 0));
        widthField = new JTextField();
        JLabel xLabel = new JLabel("X", SwingConstants.CENTER);
        heightField = new JTextField();
        
        // properties
        itemTitle.setBounds(GAP, GAP, WIDTH-GAP*2, GAP*2);
        imageLabel.setBounds(GAP, GAP*4, WIDTH-GAP*2, WIDTH-GAP*2);
        
        int panelHeight = (WIDTH-GAP*4)/3;
        countPanel.setBounds(GAP, WIDTH + GAP*3, WIDTH-GAP*2, panelHeight);
        countPanel.setOpaque(false);
        dimensionPanel.setBounds(GAP, WIDTH + GAP*5 + panelHeight, WIDTH-GAP*2, panelHeight);
        dimensionPanel.setOpaque(false);
        
        minusButton.addActionListener(this);
        addButton.addActionListener(this);
        countField.addActionListener(this);
        widthField.addActionListener(this);
        heightField.addActionListener(this);
        
        this.add(itemTitle);
        this.add(imageLabel);
        
        countPanel.add(minusButton);
        countPanel.add(countField);
        countPanel.add(addButton);
        this.add(countPanel);
        
        dimensionPanel.add(widthField);
        dimensionPanel.add(xLabel);
        dimensionPanel.add(heightField);
        this.add(dimensionPanel);
        
        this.setVisible(false);
    }
    
    private ImageIcon getScaledImage(String itemName) {
        ImageIcon imageIcon = new ImageIcon("images/"+itemName+".png");
        Image image = imageIcon.getImage().getScaledInstance(WIDTH-GAP*2, WIDTH-GAP*2, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
    
    private void updateItemCount(String loc, int index, String item) {
        int count = inv.getItemCount(loc, index, item);
        countField.setText(Integer.toString(count));
    }
    
    public void update(String loc, int index, String item) {
        itemTitle.setText(item);
        imageLabel.setBackground(Color.red);
        imageLabel.setIcon(getScaledImage(item));
        
        this.updateItemCount(loc, index, item);
        
        String name = String.format("%s|%d|%s", loc, index, item);
        minusButton.setName(name);
        addButton.setName(name);
        countField.setName(name);
        
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent) e.getSource(); // JComponent for convience
        String[] info = source.getName().split("\\|");
        String loc = info[0];
        int slot = Integer.parseInt(info[1]);
        String item = info[2];
        
        int count = inv.getItemCount(loc, slot, item);
        
        // checks handled in setItemCount()`
        if (source == minusButton) {
            count--;
        } else if (source == addButton) {
            count++;
        } else if (source == countField) {
            count = Integer.parseInt(((JTextField) source).getText());
        }
        inv.setItemCount(loc, slot, item, count);
        
        this.updateItemCount(loc, slot, item);
    }
}
