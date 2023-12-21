package com.tagsofproject;

import javax.swing.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main extends JFrame {
    public Main() {
        setTitle("Image Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Load the image from file
            BufferedImage image = ImageIO.read(new File("C:\\Users\\smadh\\Documents\\Coding\\tags\\mrfieldsroomtopdown.png"));

            // Set JFrame size based on image dimensions
            setSize(image.getWidth(), image.getHeight());

            // Create a layered pane
            JLayeredPane layeredPane = new JLayeredPane();

            // Add the image to the layered pane at depth 0
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(), image.getHeight());
            layeredPane.add(imageLabel, Integer.valueOf(0));

            // Add a JButton to the layered pane at depth 1
            JButton toolbutton = new JButton("Handheld Power Tools");
            toolbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Hello World");
                }
            });
            toolbutton.setBounds(150, 125, 200, 40); // Adjust the size as needed
            layeredPane.add(toolbutton, Integer.valueOf(1));

            // Set the content pane to the layered pane
            setContentPane(layeredPane);

        } catch (IOException e) {
            e.printStackTrace();
        }

        setLocationRelativeTo(null); // Center the JFrame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
