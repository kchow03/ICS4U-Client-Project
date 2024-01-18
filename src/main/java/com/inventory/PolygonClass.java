package com.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class PolygonClass extends JFrame implements MouseListener {
    private PolygonButton[] buttons = new PolygonButton[100]; // 100 hard limit
    private int buttonsCount = 0;
    private int addButton = 0;
    private Point[] points = new Point[4];

    public PolygonClass() {
        this.setSize(400, 400);
                
        this.addMouseListener(this);
        this.setVisible(true);
    }
    
//     from chatgpt
    public boolean isValidPolygon() {
        return true;
    }
    
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    
    public String getButton(Point p) {
        for (int i = 0; i < buttonsCount; i++) {
            if (buttons[i].isWithin(p)) return buttons[i].getName();
        }
        
        return null;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        String button = getButton(p);

        if (addButton < 4) { // add button
            points[addButton] = p;
            
            if (addButton == 3) { // last button
                if (isValidPolygon()) {
                    System.out.println("gg");
                    
                    Polygon poly = new Polygon();
                    
                    for (Point point: points) {
                        poly.addPoint(point.x, point.y);
                    }

                    buttons[buttonsCount] = new PolygonButton(poly);
                    this.add(buttons[buttonsCount]);
                    
                    buttons[buttonsCount].repaint();
                    buttonsCount++;
                    
                    this.revalidate();
                } else {
                    System.out.println("bad gg");
                    addButton = 0;
                }
            }
            addButton++;
        } else if (button != null) {
            System.out.println(button);
        }
    }
}

class PolygonButton extends JComponent implements MouseMotionListener {
  public final static Color ACTIVE_COLOR = Color.red;
  public final static Color INACTIVE_COLOR = Color.darkGray;
  private final Polygon polygon;
  private boolean isActive;

  public PolygonButton(Polygon p) {
    polygon = p;
    this.setName("e");
    
    setOpaque(false);
    
    this.addMouseMotionListener(this); 
  }
  
  public boolean isWithin(Point p) {
      return polygon.contains(p);
  }
  
  @Override
  public void mouseMoved(MouseEvent e) {
      Point p = e.getPoint();
      if (!isWithin(p)) { // outside
          if (isActive) {
              isActive = false;
              repaint();
          }
          return; // quickly return
      }
      
      if (!isActive) {
          isActive = true;
          repaint();
      }
  }
  
  @Override
  public void mouseDragged(MouseEvent e) {}
  
  @Override
  public void paint(Graphics g) {
      System.out.println("hi");
    g.setColor(isActive ? ACTIVE_COLOR : INACTIVE_COLOR);
    g.drawPolygon(polygon);
  }

}