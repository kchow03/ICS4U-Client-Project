package com.inventory;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.json.JSONObject;

public class GUI extends JFrame implements ActionListener {
    public static final String[] BUTTON_NAME = { "Handheld Power Tools", "Blue Cabinet", "Big Cabinet", "Mr Field Cabinet" };
    public static final int[][] BUTTON_DATA = {
        { 150, 125, 200, 40 },
        { 750, 400, 200, 40 },
        { 750, 600, 200, 40 },
        { 120, 600, 200, 40 }
    };
    private Inventory inventory;
    private JButton[] buttons;
    private String itemName;
    private String locName;
    
    public GUI() {
        inventory = new Inventory();
        genButtons();

        this.setTitle("Image Display");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();

        // init buttons
        for (JButton button: buttons) {
            layeredPane.add(button, 1);
        }

        try {
            // Load the image from file
            BufferedImage image = ImageIO.read(new File("topdown.png"));

            // Set JFrame size based on image dimensions
            setSize(image.getWidth(), image.getHeight());

            // Add the image to the layered pane at depth 0
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(), image.getHeight());
            layeredPane.add(imageLabel, 0);

            this.setContentPane(layeredPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setLocationRelativeTo(null); // Center the JFrame
        this.setVisible(true);
    }

    private void genButtons() {
        buttons = new JButton[BUTTON_NAME.length];
        
        for (int i = 0; i < BUTTON_NAME.length; i++) {
            buttons[i] = new JButton(BUTTON_NAME[i]);
            buttons[i].setBounds(BUTTON_DATA[i][0], BUTTON_DATA[i][1], BUTTON_DATA[i][2], BUTTON_DATA[i][3]);
            buttons[i].addActionListener(this);
        }
    }

    private void updatePopup(String loc) {
        JFrame popup = new JFrame();
        popup.setSize(400, 400);
        popup.repaint();
        popup.setTitle(loc);

        JPanel invPanel = new JPanel();
        invPanel.setLayout(new BoxLayout(invPanel, BoxLayout.Y_AXIS));
        
        JSONObject locData = inventory.getLoc(loc);

        String[] keys = JSONObject.getNames(locData);
        for (int i = 0; i < locData.length(); i++) {
            JButton button = new JButton(String.format("%s: %d", keys[i], locData.getInt(keys[i])));
            button.setMaximumSize(new Dimension(400, 50));
            button.addActionListener(this);
            invPanel.add(button);
        }

        JScrollPane scroll = new JScrollPane(invPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(25, 100, invPanel.getWidth() - 50, invPanel.getHeight() - 125);

        popup.add(scroll);
        popup.setVisible(true);    
    }

    private void updateItemMenu(String text) {
        JFrame itemMenu = new JFrame();
        itemMenu.setLayout(null);
        itemMenu.setSize(200, 200);

        String item = text.split(":")[0];
        itemMenu.setTitle(item);
        
        JLabel label = new JLabel(text);
        label.setBounds(0, 0, 100, 200);
        itemMenu.add(label);

        JButton add = new JButton("+ 1");
        add.setBounds(90, 0, 80, 80);
        add.addActionListener(this);
        itemMenu.add(add);

        JButton minus = new JButton("- 1");
        minus.setBounds(90, 90, 80, 80);
        minus.addActionListener(this);
        itemMenu.add(minus);

        itemMenu.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String text = button.getText();
        String item = text.split(":")[0];

        if (inventory.isValidLoc(text)) {
            updatePopup(text);
            locName = text;
        } else if (inventory.isValidItem(item)) {
            updateItemMenu(text);
            itemName = item;
        } else { // item menu buttons
            if (text.equals("+ 1")) {
                inventory.changeVal(locName, itemName, 1);
            } else {
                inventory.changeVal(locName, itemName, -1);
            }
            updatePopup(locName);
            updateItemMenu(itemName);
            
        }
    }

    public static void main(String[] args) {
        new GUI();
    }
}
