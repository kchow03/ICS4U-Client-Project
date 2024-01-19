package com.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import javax.swing.JComponent;

public class LocationButton extends JComponent implements MouseMotionListener {
  public final static Color ACTIVE_COLOR = Color.red;
  public final static Color INACTIVE_COLOR = Color.darkGray;
  private final Polygon polygon = new Polygon();
  private boolean isActive;

  public LocationButton(Point[] points, String name) {
      orderPolygon(points);
      this.setName(name);
      this.addMouseMotionListener(this); 
    
//    setOpaque(false);
    
    
  }
  
  // from chat gpt
    private void orderPolygon(Point[] points) {
        double cx = 0, cy = 0;
        for (Point point : points) {
            cx += point.x;
            cy += point.y;
        }
        cx /= points.length;
        cy /= points.length;
        
        final double cyf = cy, cxf = cx; // must be finals

        Arrays.sort(points, (p1, p2) -> {
            double angle1 = Math.atan2(p1.y - cyf, p1.x - cxf);
            double angle2 = Math.atan2(p2.y - cyf, p2.x - cxf);
            return Double.compare(angle1, angle2);
        });
        
        for (Point p: points) {
            polygon.addPoint(p.x, p.y);
        }
    }
  
  public boolean isWithin(Point p) {
      return polygon.contains(p);
  }
  
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
      super.paint(g);
      System.out.println("hi");
    g.setColor(isActive ? ACTIVE_COLOR : INACTIVE_COLOR);
    g.drawPolygon(polygon);
  }

}