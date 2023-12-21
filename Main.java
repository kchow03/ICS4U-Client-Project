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

            //Power tools popup.
            
            JFrame frame = new JFrame("Power Tools Label Example");
            frame.setSize(400, 400); // Set the size of the frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Add a JButton to the layered pane at depth 1
            JButton toolbutton = new JButton("Handheld Power Tools");
            toolbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JLabel powerToolsLabel = new JLabel("handheld power tools");
                    powerToolsLabel.setBounds(350, 120, 200, 200); // Set bounds (x, y, width, height)
                    powerToolsLabel.setFont(powerToolsLabel.getFont().deriveFont(20.0f)); // Set font size
                    frame.add(powerToolsLabel);
                    frame.setVisible(true);
                }
            });
            toolbutton.setBounds(150, 125, 200, 40); // Adjust the size as needed
            layeredPane.add(toolbutton, Integer.valueOf(1));

            //blue cabinet button and popup
            JFrame frame2 = new JFrame("Blue Cabinet Label Example");
            frame2.setSize(400, 400); // Set the size of the frame
            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Add a JButton to the layered pane at depth 1
            JButton blueButton = new JButton("Blue Cabinet");
            blueButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JLabel powerToolsLabel = new JLabel("Blue cabinet");
                    powerToolsLabel.setBounds(350, 120, 200, 200); // Set bounds (x, y, width, height)
                    powerToolsLabel.setFont(powerToolsLabel.getFont().deriveFont(20.0f)); // Set font size
                    frame2.add(powerToolsLabel);
                    frame2.setVisible(true);
                }
            });
            blueButton.setBounds(750, 400, 200, 40); // Adjust the size as needed
            layeredPane.add(blueButton, Integer.valueOf(1));

            // Big Cabinet Button
            JFrame frame3 = new JFrame("Big Cabinet Label Example");
            frame3.setSize(400, 400); // Set the size of the frame
            frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JButton bigButton = new JButton("Big Cabinet");
            bigButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JLabel bigLabel = new JLabel("Big cabinet");
                    bigLabel.setBounds(350, 120, 200, 200); // Set bounds (x, y, width, height)
                    bigLabel.setFont(bigLabel.getFont().deriveFont(20.0f)); // Set font size
                    frame3.add(bigLabel); 
                    frame3.setVisible(true);
                }
            });
            bigButton.setBounds(750, 600, 200, 40); // Adjust the size as needed
            layeredPane.add(bigButton, Integer.valueOf(1));

            // Big Cabinet Button
            JFrame frame4 = new JFrame("Mr Field Cabinet Label Example");
            frame4.setSize(400, 400); // Set the size of the frame
            frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JButton MFButton = new JButton("Mr Field Cabinet");
            MFButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JLabel MFlabel = new JLabel("Mr Field cabinet");
                    MFlabel.setBounds(350, 120, 200, 200); // Set bounds (x, y, width, height)
                    MFlabel.setFont(MFlabel.getFont().deriveFont(20.0f)); // Set font size
                    frame4.add(MFlabel); 
                    frame4.setVisible(true);
                }
            });
            MFButton.setBounds(120, 600, 200, 40); // Adjust the size as needed
            layeredPane.add(MFButton, Integer.valueOf(1));

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
