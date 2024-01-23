package com.inventory;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Handler extends JFrame implements ActionListener, MouseListener, ChangeListener {
    public static final Color BACKGROUND = Color.decode("#231B1D");
    public static final Color SECONDARY = Color.decode("#5C5845");
    public static final Color FOREGROUND = Color.decode("#F4F0F1");
    public static final Color THIRD = Color.decode("#9DA283");
    public static final String FOLDER = System.getenv("APPDATA") + "/com.inventory/"; // pray does not already exist;
    Inventory inventory;
    JPanel cards; // cannot change layout of jframe
    boolean addLocation;
    boolean remLocation;
    ArrayList<int[]> points;
    
    public Handler() {       
        // attempt to set ui type
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {}
        
        setResizable(false); // for ease
        setTitle("Inventory");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        cards = new JPanel(new CardLayout());
                
        try {
            File imageFile = new File(FOLDER + "topdown.png");
            Image image = ImageIO.read(imageFile);
            ImageIcon backgroundImage = new ImageIcon(image);
            
            inventory = new Inventory();
            addLocation = false;
            points = new ArrayList<>();
            
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    inventory.save(); // saves changes
                }
            });
            
            JPanel test = new JPanel();
            test.setBackground(Color.red);
            
            String[] cardNames = { "locations", "slots", "items", "addItem" };
            JComponent[] cardPanels = {
                new LocationsPanel(this, inventory, image),
                new SlotsPanel(this, inventory),
                new ItemsPanel(this, inventory),
                new AddItemPanel(this, inventory)
            };
            
            for (int i = 0; i < cardPanels.length; i++) { // cardpanels for testing
                cards.add(cardPanels[i], cardNames[i]);
            }
            
            setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        } catch (IOException e) { // file doesn't exist
            File folder = new File(FOLDER);
            folder.mkdir(); // create folder for future use
            
            String[] folders = {"Locations", "Items"};
            for (String subFolder: folders) {
                new File(FOLDER, subFolder).mkdir();
            }
            
            setSize(640, 480);
            cards.add(new JLabel("Not found: %appdata%/com.inventory/topdown.png"));
        }
        
        add(cards);
        setLocationRelativeTo(null); // centers
        setVisible(true);
    }
    
    // required abstrqact classes
    @Override
    public void mouseExited(MouseEvent e) {};
    @Override
    public void mouseEntered(MouseEvent e) {};
    @Override
    public void mouseReleased(MouseEvent e) {};
    @Override
    public void mouseClicked(MouseEvent e) {};
    
    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println(e);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        LocationsPanel panel = (LocationsPanel) e.getSource(); // always from locations
        String active = panel.getActive();
        
        if (addLocation) {
            int[] point = {e.getX(), e.getY()};
            points.add(point);
            
            Graphics g = panel.getGraphics();
            g.setColor(Color.RED);
            for (int[] p: points) {
                g.fillRect(p[0], p[1], 5, 5);
            }            
            
            if (points.size() == 4) { // 4 buttons
                addLocation = false;
                panel.showLocationNamePanel();
            }
        } else if (active != null ) { // any are hovered
            inventory.setLocation(active);
            if (remLocation) { // remove location
                inventory.remLocation();
                panel.remLocation();
            } else {
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, "slots");
            }
        }
    };
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
                
        switch (action) {
            case "addLocation" -> {
                JButton source = (JButton) e.getSource();
                LocationsPanel panel = (LocationsPanel) source.getParent().getParent().getParent();
                
                panel.darken();
                panel.removeMouseMotionListener(panel);
                
                addLocation = true;
                points.clear();
            } case "remLocation" -> {
                JButton source = (JButton) e.getSource();
                LocationsPanel panel = (LocationsPanel) source.getParent().getParent().getParent();
                
                panel.darken();
                remLocation = true;
            } case "addSlot" -> {
                
//            } case "" -> {
                
//            } case "" -> {
                
            } default -> { // jtextfield
                if (!(e.getSource() instanceof JTextField)) { // destination
                    String[] actionInfo = e.getActionCommand().split("\\|");
                    String destination = actionInfo[0];
                    
                    switch (destination) {
                        case "items" -> {
                            inventory.setSlot(Integer.parseInt(actionInfo[1]));
                        } 
                    }
                    
                    CardLayout cl = (CardLayout) cards.getLayout();
                    cl.show(cards, destination);
                } else {
                    JTextField source = (JTextField) e.getSource();
                    String name = source.getName();
                    switch (name) {
                        case "locationName" -> {
                            LocationsPanel panel = (LocationsPanel) source.getParent().getParent().getParent();

                            inventory.setLocation(action);
                            if (!inventory.hasLocation()) {
                                panel.addLocation(points);
                                inventory.setLocation(points);
                            } else {
                                panel.invalidName();
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Handler();
            }
        });
    }
}
