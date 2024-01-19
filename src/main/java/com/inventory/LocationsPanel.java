package com.inventory;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EventListener;
import javax.imageio.ImageIO;

public class LocationsPanel extends JLabel {
    private final int WIDTH;
    private final int HEIGHT;
    private final ImageIcon image;
    private final ImageIcon darkened;
    
    public LocationsPanel(EventListener listener, int w, int h, Inventory inv, String imagePath) {
        WIDTH = w;
        HEIGHT = h;
        
        image = new ImageIcon(imagePath);
        darkened = getDarkenedImage(imagePath);
        
        // set label properties
        this.setSize(WIDTH, HEIGHT);
        this.setIcon(image);
        
        // load buttons
        for (String loc: inv.getLocations()) {
            JButton button = new JButton(loc + ": " + inv.getNumSlots(loc));
            button.setBounds(inv.getBounds(loc));
            button.setName("location|%s".formatted(loc));
            button.addActionListener((ActionListener) listener);
            this.add(button);
        }
    }
    
    // from chatgpt
    private ImageIcon getDarkenedImage(String imagePath) {        
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
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
        } catch (IOException e) {
            return new ImageIcon();
        }
    }
    
    public void darken() {
        this.setIcon(darkened);
    }
    
    public void reset() {
        this.setIcon(image);
    }
}