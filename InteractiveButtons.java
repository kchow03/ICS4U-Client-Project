/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.interactivebuttons;

/**
 *
 * @author smadh
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

class ShapedButton extends JButton {
    private Path2D path;
    private boolean isActive;

    public ShapedButton() {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setText("Click me");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isActive = !isActive;
                repaint();
                setText(isActive ? "Clicked!" : "Click me");
            }
        });
    }

    public void setPath(Path2D path) {
        this.path = path;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (path != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(isActive ? Color.RED : Color.DARK_GRAY);
            g2d.fill(path);
            g2d.setColor(Color.BLACK);
            g2d.draw(path);
            g2d.dispose();
        }
    }
}

public class InteractiveButtons extends JFrame {
    private ShapedButton customButton;
    private Point[] corners = new Point[4];
    private int cornerCount = 0;

    public InteractiveButtons() {
        setTitle("Custom Button Example");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        customButton = new ShapedButton();
        add(customButton);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cornerCount < 4) {
                    corners[cornerCount] = e.getPoint();
                    cornerCount++;
                    repaint();
                }
            }
        });

        setLayout(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (cornerCount == 4) {
            Path2D.Double path = new Path2D.Double();
            path.moveTo(corners[0].x, corners[0].y);

            for (int i = 1; i < corners.length; i++) {
                path.lineTo(corners[i].x, corners[i].y);
            }

            path.closePath();
            customButton.setBounds(path.getBounds());
            customButton.setPath(path);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InteractiveButtons());
    }
}
