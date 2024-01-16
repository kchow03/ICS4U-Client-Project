package com.inventory;

import javax.swing.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import java.util.Arrays;
import java.util.Comparator;

public class SlotsPanel extends Panel {
    private final JButton imageButton;
    private final JLabel imagePreview;
    private final JLabel locationTitle;
    private final JPanel slotsPanel;
    private final GridLayout layout;
    private final JComboBox sortBox;
    private final JSlider columnSlider;
    
    public SlotsPanel(ActionListener actionLister, ChangeListener changeListener, int w, int h) {
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
        
        JLabel sortByLabel = new JLabel("Sort by: ");
        JLabel columnLabel = new JLabel("Columns: ");
        sortBox = new JComboBox(GUI.SORT_METHODS);
        columnSlider = new JSlider(1, 5);
        
        int contentGap = WIDTH/5*3+GUI.GAP;
        int contentWidth = WIDTH/5*2-GUI.GAP*3;
        locationTitle.setBounds(contentGap, GUI.GAP, contentWidth, GUI.GAP*3);
        scrollable.setBounds(contentGap, GUI.GAP*8, contentWidth, HEIGHT-GUI.GAP*12);
        slotsPanel.setBackground(Color.decode("#1c1617"));
        
        locationTitle.setForeground(Color.decode("#f4f0f1"));
        locationTitle.setBackground(Color.decode("#1c1617"));
        locationTitle.setOpaque(true);
        
        sortByLabel.setBounds(contentGap, GUI.GAP*5, GUI.GAP*3, GUI.GAP*2);
        sortByLabel.setBackground(Color.decode("#1c1617"));
        sortByLabel.setForeground(Color.decode("#f4f0f1"));
        sortByLabel.setOpaque(true);
        columnLabel.setBounds(WIDTH-GUI.GAP*11, GUI.GAP*5, GUI.GAP*4, GUI.GAP*2);
        columnLabel.setBackground(Color.decode("#1c1617"));
        columnLabel.setForeground(Color.decode("#f4f0f1"));
        columnLabel.setOpaque(true);
        
        sortBox.setName("sortSlots");
        sortBox.setBounds(contentGap+GUI.GAP*3, GUI.GAP*5, GUI.GAP*8, GUI.GAP*2);
        sortBox.addActionListener(actionLister);
        columnSlider.setBounds(WIDTH-GUI.GAP*7, GUI.GAP*5, GUI.GAP*5, GUI.GAP*2);
        columnSlider.setBackground(Color.decode("#1c1617"));
        columnSlider.addChangeListener(changeListener);
        columnSlider.setName("slots");
        
        imageButton.setSize(WIDTH/5*3, HEIGHT);
        imageButton.setLayout(null);
        imageButton.setName("image");
        imageButton.addActionListener(actionLister);
        
        imagePreview.setSize(WIDTH/5*3, HEIGHT);
        
        imagePreview.add(backButton);
        imageButton.add(imagePreview);
        
        this.add(sortByLabel);
        this.add(columnLabel);
        this.add(imageButton);
        this.add(locationTitle);
        this.add(sortBox);
        this.add(columnSlider);
        this.add(scrollable);
    }
    
    public void update(ActionListener actionListener, Inventory inv, String loc) {
        String path = String.format("%s/%s/%s.png", GUI.FOLDER, "Locations", loc); 
        ImageIcon image = new ImageIcon(path);
        Image scaledImage = image.getImage().getScaledInstance(WIDTH/5*3, HEIGHT, Image.SCALE_SMOOTH);
        
        locationTitle.setText(loc);
        imagePreview.setIcon(new ImageIcon(scaledImage));
        
        String sort = inv.getSort(loc);
        columnSlider.setValue(inv.getLocationColumns(loc));
        sortBox.setSelectedIndex(Arrays.asList(GUI.SORT_METHODS).indexOf(sort));
                
        int sortMethod = Arrays.asList(GUI.SORT_METHODS).indexOf(sort);
        sortButtons(actionListener, inv, loc, sortMethod); // for not overwriting sort and filter
        updateColumns(inv.getLocationColumns(loc));
    }
    
    public void sortButtons(ActionListener actionListener, Inventory inv, String loc, int sort) {
        slotsPanel.removeAll();
        
        int numSlots = inv.getNumSlots(loc);
        GridButton[] buttons = new GridButton[numSlots];
        
        int columns = inv.getLocationColumns(loc);
        int size = (WIDTH/5*2-GUI.GAP*3-GUI.GAP*(columns-1))/columns;
        for (int i = 0; i < numSlots; i++) {
            int numItems = inv.getNumItems(loc, i);
            int numTotalItems = inv.getNumTotalItems(loc, i);
            
            // accepts html for line break
            String text = "<html>Slot: %d<br>Item count: %d<br>Total item count: %d</html>".formatted(i, numItems, numTotalItems);
            String name = "slot|%d|%d|%d".formatted(i, numItems, numTotalItems);
            GridButton slotButton = new GridButton(actionListener, name, text, size);
            buttons[i] = slotButton;
        }        
        
        Comparator<GridButton> comparator = Comparator.comparingInt(button -> {
            String value = button.button.getName().split("\\|")[sort+1]; // first is slot
            
            return Integer.parseInt(value);
        });
        Arrays.sort(buttons, comparator);
        
        for (GridButton slotButton: buttons) {
            slotsPanel.add(slotButton);
        }
        
        GridButton addButton = new GridButton(actionListener, "addSlot", "+", size);
        slotsPanel.add(addButton);
        
        slotsPanel.revalidate();
        slotsPanel.repaint();
    }
    
    public void updateColumns(int columns) {
        layout.setColumns(columns);
        
        // refresh
        slotsPanel.revalidate();
        slotsPanel.repaint();
    }
}
