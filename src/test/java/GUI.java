package com.inventory;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame implements ActionListener, ChangeListener, MouseListener {
    public static final Color backgroundColour = Color.decode("#130e0f");
    public static final Color secondaryColour = Color.decode("#1c1617");
    public static final Color textColour = Color.decode("#5c5845");
    public static final String FOLDER = "Images";
    public static final String[] SORT_METHODS = ; 
    public static final int GAP = 15;
    private final int WIDTH;
    private final int HEIGHT;
    private final Inventory inv;
    private final LocationsPanel locationsPanel;
    private final SlotsPanel slotsPanel;
    private final ItemsPanel itemsPanel;
    private final ToolsPanel toolsPanel;
    private final AddItemPanel addItemPanel;
    private final JButton toolsButton;
    private String location;
    private int slot;
    private String item;
    private String current;
    private Point[] points = new Point[4];
    private JPanel[] plots = new JPanel[3];
    private int pointCount = 4;
    private LocationButton[] locationButtons = new LocationButton[128]; // hard limit
    private int LBC = 0; // locationButtonCount
    
    public GUI() {
        // set ui type
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            
        }
        
        ImageIcon image = new ImageIcon("topdown.png");
        WIDTH = image.getIconWidth();
        HEIGHT = image.getIconHeight() - 30; // exclude topbar
        inv = new Inventory();
        
        // setup jframe
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                inv.save();
            }
        });
        
        locationsPanel = new LocationsPanel(this, WIDTH, HEIGHT, inv, "topdown.png");
        slotsPanel = new SlotsPanel(this, WIDTH, HEIGHT);
        itemsPanel = new ItemsPanel(this, WIDTH, HEIGHT);
        toolsPanel = new ToolsPanel(this);
        toolsButton = new JButton();
        addItemPanel = new AddItemPanel(this, WIDTH, HEIGHT);
        
        ImageIcon imageIcon = new ImageIcon(FOLDER + "/Settings.png");
        Image toolsImage = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        toolsButton.setIcon(new ImageIcon(toolsImage));
        toolsButton.setBounds(WIDTH - (50+GAP), HEIGHT - (50+GAP), 50, 50);
        toolsButton.setName("settings");
        toolsButton.addActionListener(this);
        toolsButton.setContentAreaFilled(false);
        
        locationsPanel.add(toolsButton);
        this.add(locationsPanel);
        this.add(slotsPanel);
        this.add(itemsPanel);
        this.add(addItemPanel);
        this.setVisible(true);
    }
    
    private void hidePanels(JComponent panel) {
        locationsPanel.setVisible(false);
        slotsPanel.setVisible(false);
        itemsPanel.setVisible(false);
        addItemPanel.setVisible(false);
        
        panel.setVisible(true);
    }
    
    // abstract classes
    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    
    
    public void mouseClicked(MouseEvent e) {
        if (pointCount < 4) {
            points[pointCount] = e.getPoint();
            
            if (pointCount == 3) { // last plot placed
                for (int i = 0; i < 3; i++) {
                    plots[i].setVisible(false);
                }
                locationsPanel.reset();
                
                locationButtons[LBC] = new LocationButton(points, "e");
                locationsPanel.add(locationButtons[LBC]);
                locationButtons[LBC].repaint();
                locationButtons[LBC].revalidate();
                LBC++;
                
            } else {
                plots[pointCount] = new JPanel();
                plots[pointCount].setBounds(points[pointCount].x, points[pointCount].y, 5, 5);
                plots[pointCount].setBackground(Color.RED);
                locationsPanel.add(plots[pointCount]);
                locationsPanel.repaint();
            }
            
            pointCount++;
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();
        String[] name = component.getName().split("\\|");
        String type = name[0];
        String data = null;
        int count;
                
        if (type.equals("back")) {
            switch (current) {
                case "location":
                    current = "home";
                    break;
                case "slot":
                    current = "location";
                    break;
                case "item":
                    current = "slot";
                    break;
                case "addItem":
                    current = "item";
                    break;
            }
        }
        
        switch (type) {
            case "image":
                
                break;
            case "sortSlots":
                String selected = (String) ((JComboBox) e.getSource()).getSelectedItem();
                inv.setLocationSort(location, selected);
                slotsPanel.update(inv, location);
                break;
            case "settings":
                toolsPanel.setVisible(true);
                
                break;
                
            case "addLocation":
                pointCount = 0;
                locationsPanel.darken();
                toolsPanel.setVisible(false);
                break;
            case "remLocation":
                
                break;
            case "addItemCount":
                count = inv.getItemCount(location, slot, item)+1;
                inv.setItemCount(location, slot, item, count);
                itemsPanel.update(item, count);
                break;
            case "subItemCount":
                count = inv.getItemCount(location, slot, item)-1;
                inv.setItemCount(location, slot, item, count);
                itemsPanel.update(item, count);
                break;
            case "itemCount":
                count = Integer.parseInt(((JTextField) e.getSource()).getText());
                inv.setItemCount(location, slot, item, count);
                itemsPanel.update(item, count);
                break;
            case "submit":
                inv.addItem(location, slot, addItemPanel.getItemName(), addItemPanel.getItemCount());
                
                hidePanels(itemsPanel);
                itemsPanel.refresh(this, inv, location, slot);
                break;
            default:
                if (!type.equals("back")) {
                    current = type;
                    data = name[1];
                }
                
                switch (current) {
                    case "home":
                        this.hidePanels(locationsPanel);
                        break;
                    case "location":
                        if (data != null) location = data;

                        this.hidePanels(slotsPanel);
                        slotsPanel.refresh(this, inv, location);
                        break;
                    case "slot":
                        if (data != null) slot = Integer.parseInt(data);

                        this.hidePanels(itemsPanel);
                        itemsPanel.refresh(this, inv, location, slot);
                        break;
                    case "item":
                        if (data != null) item = data;
                        
                        itemsPanel.update(item, inv.getItemCount(location, slot, item));
                        break;
                    case "addItem":
                        addItemPanel.reset();
                        this.hidePanels(addItemPanel);
                        break;
                }
                break;
            }
        }
    
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource(); // always slider
        String type = source.getName();
        
        if (type.equals("slots")) {
            int value = source.getValue();
        
            inv.setLocationColumns(location, value);
            slotsPanel.update(inv, location);
        }   
    }
}