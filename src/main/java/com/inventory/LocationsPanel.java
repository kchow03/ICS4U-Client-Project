package com.inventory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EventListener;

public class LocationsPanel extends JLabel implements MouseMotionListener, ActionListener {
    final static Color ACTIVE_COLOR = new Color(255, 0, 0, 128);
    final static Color INACTIVE_COLOR = new Color(64, 64, 64, 128);
    private final ImageIcon background;
    private final ImageIcon darkened;
    Inventory inv;
    ArrayList<String> locations;
    ArrayList<Polygon> buttons;
    ArrayList<Boolean> states;
    JPanel settingsPanel;
    JButton settingsButton;
    JPanel controlButtonsPanel;
    JPanel locationNamePanel;
    JTextField locationNameField;
    
    
    public LocationsPanel(EventListener listener, Inventory inv, Image image) {
        background = new ImageIcon(image);
        darkened = getDarkenedImage(image);
        this.inv = inv;
        
        // set background image
        setIcon(background);
        addMouseMotionListener(this);
        addMouseListener((MouseListener) listener);
        setLayout(new BorderLayout());
    
        

        // load buttons
        locations = inv.getLocations();
        states = new ArrayList<>();
        buttons = new ArrayList<>();
        for (String loc: locations) {
            Polygon poly = new Polygon();
            
            inv.setLocation(loc);
            for (int[] p: inv.getLocationPoints()) {
                poly.addPoint(p[0], p[1]);
            }
            buttons.add(poly);
        }
        for (int i = 0; i < locations.size(); i++) {
            states.add(false);
        }
        
        // name
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
                
        locationNamePanel = new JPanel(new GridLayout(2, 1, 5, 0));
        locationNamePanel.setPreferredSize(new Dimension(150, 75));
        locationNamePanel.setBackground(Handler.BACKGROUND);
        locationNamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        locationNamePanel.setVisible(false);
        
        JLabel locationNameLabel = new JLabel("Location name:");
        locationNameLabel.setForeground(Handler.FOREGROUND);
        locationNamePanel.add(locationNameLabel);
        
        locationNameField = new JTextField();
        locationNameField.setBackground(Handler.SECONDARY);
        locationNameField.setForeground(Handler.FOREGROUND);
        locationNameField.setName("locationName");
        locationNameField.addActionListener((ActionListener) listener);
        locationNamePanel.add(locationNameField);
        
        centerPanel.add(locationNamePanel);
        
        
        
        // settings button
        Image settingsImage = new ImageIcon("Settings.png").getImage();
        ImageIcon settingsIcon = new ImageIcon(settingsImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        
        settingsButton = new JButton();
        settingsButton.setPreferredSize(new Dimension(50, 0));
        settingsButton.setContentAreaFilled(false); // transparent background
        settingsButton.setActionCommand("showSettings");
        settingsButton.addActionListener(this);
        settingsButton.setIcon(settingsIcon);
        
        JPanel settingsButtonPanel = new JPanel(new BorderLayout());
        settingsButtonPanel.setPreferredSize(new Dimension(0, 50));
        settingsButtonPanel.setOpaque(false);
        settingsButtonPanel.add(settingsButton, BorderLayout.EAST);
        
        controlButtonsPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        controlButtonsPanel.setPreferredSize(new Dimension(0, 200));
        controlButtonsPanel.setOpaque(false);
        controlButtonsPanel.setVisible(false);
        
        JButton addLocationButton = new JButton("Add Location");
        addLocationButton.setActionCommand("addLocation");
        addLocationButton.setOpaque(false);
        addLocationButton.addActionListener((ActionListener) listener);
        controlButtonsPanel.add(addLocationButton);
        
        JButton remLocationButton = new JButton("Remove Location");
        remLocationButton.setActionCommand("remLocation");
        remLocationButton.setOpaque(false);
        remLocationButton.addActionListener((ActionListener) listener);
        controlButtonsPanel.add(remLocationButton);

        JButton backButton = new JButton("Back");
        backButton.setActionCommand("hideSettings");
        backButton.setOpaque(false);
        backButton.addActionListener(this);
        controlButtonsPanel.add(backButton);
        
        settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setBackground(Handler.BACKGROUND);
        settingsPanel.setPreferredSize(new Dimension(200, 0));
        settingsPanel.add(settingsButtonPanel, BorderLayout.SOUTH);
        settingsPanel.add(controlButtonsPanel, BorderLayout.NORTH);
        settingsPanel.setOpaque(false);
        
        
        add(centerPanel, BorderLayout.CENTER);
        add(settingsPanel, BorderLayout.EAST);
    }
    
    // from chatgpt
    private ImageIcon getDarkenedImage(Image image) {        
        BufferedImage bufferedImage = (BufferedImage) image;
        BufferedImage darkenedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = darkenedImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, null);
        g.dispose();

        for (int y = 0; y < darkenedImage.getHeight(); y++) {
            for (int x = 0; x < darkenedImage.getWidth(); x++) {
                int rgb = darkenedImage.getRGB(x, y);
                Color color = new Color(rgb, true);

                // Darken the color (you can adjust the darkness level here)
                int darkerRgb = new Color(
                    Math.max(0, color.getRed() - 128),
                    Math.max(0, color.getGreen() - 128),
                    Math.max(0, color.getBlue() - 128),
                    color.getAlpha()
                ).getRGB();

                darkenedImage.setRGB(x, y, darkerRgb);
            }
        }

        return new ImageIcon(darkenedImage);
    }
    
    public String getActive() {
        for (int i = 0; i < locations.size(); i++) {
            if (states.get(i)) return locations.get(i);
        }
        
        return null; // none are active
    }
    
    public void darken() {
        setIcon(darkened);
        toggleSettings(false);
    }
    
    public void addLocation(ArrayList<int[]> points) {
        locations.add(inv.getLocationName());
        Polygon ordered = orderPolygon(points);
        buttons.add(ordered);
        states.add(false); // init state
        
        addMouseMotionListener(this);
        reset();
        locationNamePanel.setVisible(false);
    }
    
    public void remLocation() {
        int index = locations.indexOf(inv.getLocationName());
        
        locations.remove(index);
        buttons.remove(index);
        states.remove(index);
        
        reset();
    }
    
    
    
    // from chat gpt
    private static Polygon orderPolygon(ArrayList<int[]> points) {
        double cx = 0, cy = 0;
        for (int[] point : points) {
            cx += point[0];
            cy += point[1];
        }
        cx /= points.size();
        cy /= points.size();
        
        final double cyf = cy, cxf = cx; // must be finals
        
        points.sort((p1, p2) -> {
            double angle1 = Math.atan2(p1[1] - cyf, p1[0] - cxf);
            double angle2 = Math.atan2(p2[1] - cyf, p2[0] - cxf);
            return Double.compare(angle1, angle2);
        });
        
        Polygon poly = new Polygon();
        for (int[] p: points) {
            poly.addPoint(p[0], p[1]);
        }
        return poly;
    }
    
    public void showLocationNamePanel() {
        locationNameField.setText(null);
        locationNamePanel.setVisible(true);
    }
    
    public void invalidName() {
        locationNameField.setText("Invalid Location Name");
    }
    
    public void reset() {
        this.setIcon(background);
    }
    
    public void toggleSettings(boolean state) {
        settingsPanel.setOpaque(state);
        controlButtonsPanel.setVisible(state);

        settingsButton.setVisible(!state);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {};
    
    @Override
    public void mouseMoved(MouseEvent e) {
        for (int i = 0; i < locations.size(); i++) {
            states.set(i, buttons.get(i).contains(e.getX(), e.getY()));
        }
        repaint(); // possible performance issue
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // only for hiding / showing settings
        boolean state = !settingsPanel.isOpaque();
        toggleSettings(state);
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
                
        for (int i = 0; i < locations.size(); i++) {
            g2d.setColor(states.get(i) ? ACTIVE_COLOR : INACTIVE_COLOR); // incase loading
            g2d.fillPolygon(buttons.get(i));
        } 
    }
}