package com.inventory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame implements ActionListener, ChangeListener {
    public static final String FOLDER = "Images";
    public static final String[] SORT_METHODS = { "Slot", "Number of items", "Total number of items" }; 
    public static final int GAP = 15;
    private final int WIDTH;
    private final int HEIGHT;
    private final Inventory inv;
    private final LocationsPanel locationsPanel;
    private final SlotsPanel slotsPanel;
    private final ItemsPanel itemsPanel;
    private String location;
    private int slot;
    private String item;
    private String current;
    
    public GUI() {
        // set ui type
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            
        }
        
        ImageIcon image = new ImageIcon("topdown.png");
        WIDTH = image.getIconWidth();
        HEIGHT = image.getIconHeight();
        inv = new Inventory();
        
        // setup jframe
        this.setSize(image.getIconWidth(), image.getIconHeight());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // center
        this.setTitle("Inventory");
        this.setLayout(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                inv.save();
            }
        });
        
        locationsPanel = new LocationsPanel(this, WIDTH, HEIGHT, image, inv);
        slotsPanel = new SlotsPanel(this, this, WIDTH, HEIGHT);
        itemsPanel = new ItemsPanel(this, WIDTH, HEIGHT);
        
        this.add(locationsPanel);
        this.add(slotsPanel);
        this.add(itemsPanel);
        
        this.setVisible(true);
    }
    
    private void hidePanels(JComponent panel) {
        locationsPanel.setVisible(false);
        slotsPanel.setVisible(false);
        itemsPanel.setVisible(false);
        
        panel.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] name = ((JComponent) e.getSource()).getName().split("\\|");
        String type = name[0];
        String data = null;
                
        if (type.equals("back")) {
            if (current.equals("location")) {
                current = "home";
            } else if (current.equals("slot")) {
                current = "location";
            } else if (current.equals("item")) {
                current = "slot";
            }
        }
        
        if (type.equals("image")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);

            chooser.setDialogTitle("Select an image for the location preview");

            FileNameExtensionFilter restriction = new FileNameExtensionFilter("Select a .png file", "png");
            chooser.addChoosableFileFilter(restriction);

            int r = chooser.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                Path source = new File(chooser.getSelectedFile().getAbsolutePath()).toPath();
                Path destination = new File("%s/%s/%s.png".formatted(FOLDER, "Location", location)).toPath();

                try {
                    Files.copy(source, destination);
                } catch (IOException exception) {

                }

                // set the label to the path of the selected file
//                    l.setText(j.getSelectedFile().getAbsolutePath());
            }
        } else if (type.equals("sortSlots")) {
            JComboBox selectBox = (JComboBox) e.getSource();
            slotsPanel.sortButtons(this, inv, location, selectBox.getSelectedIndex());
        } else {
            if (!type.equals("back")) {
                current = type;
                data = name[1];
            }
            
            if (current.equals("home")) {
                this.hidePanels(locationsPanel);
            } else if (current.equals("location")) {
                if (data != null) location = data;
                
                this.hidePanels(slotsPanel);
                slotsPanel.update(this, inv, location);

            } else if (current.equals("slot")) {
                if (data != null) slot = Integer.parseInt(data);
                
                this.hidePanels(itemsPanel);
//                itemsPanel.update();
            } else if (current.equals("item")) {

            }
        }
        
        
        
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource(); // always slider
        String type = source.getName();
        
        if (type.equals("slots")) {
            int value = source.getValue();
        
            inv.setNumColumns(location, value);
            slotsPanel.updateColumns(value); 
        }   
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}