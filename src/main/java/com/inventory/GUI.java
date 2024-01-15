package com.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class GUI extends JFrame implements ActionListener {
    public static final String FOLDER = "Images";
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
        
        locationsPanel = new LocationsPanel(this, WIDTH, HEIGHT, image, inv);
        slotsPanel = new SlotsPanel(this, WIDTH, HEIGHT);
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
        String[] name = ((JButton) e.getSource()).getName().split("\\|");
        String type = name[0];
        String data = name[1];
                
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
        } else {
            if (!type.equals("back")) current = type;
            if (current.equals("home")) {
                this.hidePanels(locationsPanel);
            } else if (current.equals("location")) {
                if (!type.equals("back")) location = data;
                
                this.hidePanels(slotsPanel);
                slotsPanel.update(this, location, inv.getLocationColumns(location), inv.getLocationSort(location), inv.getNumSlots(location));

            } else if (current.equals("slot")) {
                if (!type.equals("back")) slot = Integer.parseInt(data);
                
                this.hidePanels(itemsPanel);
//                itemsPanel.update();
            } else if (current.equals("item")) {

            }
        }
        
        
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}

class LocationsPanel extends JLabel {
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


abstract class Panel extends JPanel {
    public final int WIDTH;
    public final int HEIGHT;
    public final BackButton backButton;
    
    
    public Panel(ActionListener actionListener, int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(null);
        this.setBackground(Color.decode("#130e0f"));
        
        backButton = new BackButton(actionListener);
        
        this.setVisible(false);
    }    
}

class SlotsPanel extends Panel {
    private final JButton imageButton;
    private final JLabel imagePreview;
    private final JLabel locationTitle;
    private final JPanel slotsPanel;
    private final GridLayout layout;
    
    public SlotsPanel(ActionListener actionLister, int w, int h) {
        super(actionLister, w, h);
        
        imageButton = new JButton();
        imagePreview = new JLabel("No image preview");
        
        
        locationTitle = new JLabel();
        layout = new GridLayout(0, 3, GUI.GAP, GUI.GAP);
        slotsPanel = new JPanel(layout);
        JScrollPane scrollable = new JScrollPane(
            slotsPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        
        int contentGap = WIDTH/5*3+GUI.GAP;
        int contentWidth = WIDTH/5*2-GUI.GAP*3;
        locationTitle.setBounds(contentGap, GUI.GAP, contentWidth, GUI.GAP*3);
        scrollable.setBounds(contentGap, GUI.GAP*5, contentWidth, HEIGHT-GUI.GAP*9);
        slotsPanel.setBackground(Color.decode("#1c1617"));
        
        locationTitle.setForeground(Color.decode("#f4f0f1"));
        locationTitle.setBackground(Color.decode("#1c1617"));
        locationTitle.setOpaque(true);
        
        imageButton.setSize(WIDTH/5*3, HEIGHT);
        imageButton.setLayout(null);
        imageButton.setName("image|null"); // null is placeholder
        imageButton.addActionListener(actionLister);
        
        imagePreview.setSize(WIDTH/5*3, HEIGHT);
        
        imagePreview.add(backButton);
        imageButton.add(imagePreview);
        
        this.add(imageButton);
        this.add(locationTitle);
        this.add(scrollable);
    }
    
    public void update(ActionListener actionListener, String loc, int columns, String sort, int slots) {
        String path = String.format("%s/%s/%s.png", GUI.FOLDER, "Locations", loc); 
        ImageIcon image = new ImageIcon(path);
        Image scaledImage = image.getImage().getScaledInstance(WIDTH/5*3, HEIGHT, Image.SCALE_SMOOTH);
        
        locationTitle.setText(loc);
        
        layout.setColumns(columns);
        slotsPanel.removeAll();
        
        int size = (WIDTH/5*2-GUI.GAP*3-GUI.GAP*(columns-1))/columns;
        for (int i = 0; i < slots; i++) {
            GridButton slotButton = new GridButton(actionListener, "slot|%d".formatted(i), Integer.toString(i), size);
            slotsPanel.add(slotButton);
        }
        GridButton addButton = new GridButton(actionListener, "addSlot|null", "+", size);
        slotsPanel.add(addButton);        
        
        
        
        imagePreview.setIcon(new ImageIcon(scaledImage));
    }
}

class ItemsPanel extends Panel {
    public ItemsPanel(ActionListener actionLister, int w, int h) {
        super(actionLister, w, h);
        
        this.add(backButton);
    }
    
}

class GridButton extends JPanel {
    public GridButton(ActionListener actionListener, String name, String text, int size) {   
        this.setLayout(new BorderLayout());
        this.setOpaque(true);
        
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(size, size));
        button.setName(name);
        button.addActionListener(actionListener);
        this.add(button);
    }
}

class BackButton extends JButton {
    public BackButton(ActionListener actionListener) {
        this.setText("BACK");
        this.setName("back|null"); // null is placeholder
        this.setBounds(GUI.GAP, GUI.GAP, GUI.GAP*4, GUI.GAP*2);
        this.addActionListener(actionListener);
        
        
        // colours
        this.setForeground(Color.decode("#5c5845"));
//        this.setOpaque(true);
//        this.setBackground(Color.decode("#5c5845"));
//        this.setForeground(Color.decode("#f4f0f1"));
    }
}