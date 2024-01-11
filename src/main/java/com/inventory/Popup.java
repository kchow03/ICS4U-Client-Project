package com.inventory;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Popup extends JFrame implements ActionListener {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 480;
    private final Inventory inv;
    private final JScrollPane scrollable;
    private final JPanel panel;
    private final ItemPanel itemPanel;
    
    public Popup(Inventory inv) {
        this.inv = inv;
        this.setResizable(false);
        this.setBounds(0, 0, WIDTH, HEIGHT);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLayout(null);
        
        scrollable = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        this.add(scrollable);
        panel = new JPanel(new GridLayout(0, 5, 25, 25));
        
        Rectangle r = new Rectangle(HEIGHT, 0, WIDTH-HEIGHT, HEIGHT);
        itemPanel = new ItemPanel(r, inv);
        this.add(itemPanel);
    }
    
    private void updateMenu(String title, Dimension d) {
        this.setTitle(title);
        itemPanel.setEnabled(false);
        scrollable.setSize(d);
    }
    
    public void slotsMenu(String loc) {
        String title = loc;
        updateMenu(title, new Dimension(WIDTH, HEIGHT));
//        this.getContentPane().setBackground(Color.GRAY);
        
        panel.removeAll();
        for (int i = 0; i < inv.getNumSlots(loc); i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setName("slot|" + i);
            button.addActionListener(this);
            panel.add(button);
        }
        scrollable.setViewportView(panel); // set panel to jscroll

        this.setVisible(true);
    }
    
    private void itemsMenu(String loc, int index) {
        String title = loc + " | " + index;
        updateMenu(title, new Dimension(WIDTH, HEIGHT));
        
        panel.removeAll();
        for (String item: inv.getItems(loc, index)) {
            JButton button = new JButton(item);
            
            ImageIcon image = new ImageIcon("images/"+item+".png");
            JLabel imageLabel = new JLabel(image);
            imageLabel.setSize(76, 76);
            button.add(imageLabel);
            
            button.setName("item|" + item);
            button.addActionListener(this);
            panel.add(button);
        }
        scrollable.setViewportView(panel); // set panel to jscroll
    }
    
    private void itemMenu(String loc, int index, String item) {
        String title = String.format("%s | %d | %s", loc, index, item);
        updateMenu(title, new Dimension(HEIGHT, HEIGHT));
        
        itemPanel.update(loc, index, item);
        
        
        
        itemPanel.setEnabled(true);
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
