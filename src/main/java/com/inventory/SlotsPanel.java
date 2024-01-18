package com.inventory;

import javax.swing.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventListener;

public class SlotsPanel extends Panel {
    private final JButton imageButton;
    private final JLabel locationTitle;
    private final JPanel slotsPanel;
    private final GridLayout layout;
    private final JComboBox sortBox;
    private final JSlider columnSlider;
    private final GridButton addButton;
    
    public SlotsPanel(EventListener listener, int w, int h) {
        super(listener, w, h);
        
        imageButton = new JButton();
        
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
        sortBox.addActionListener((ActionListener) listener);
        columnSlider.setBounds(WIDTH-GUI.GAP*7, GUI.GAP*5, GUI.GAP*5, GUI.GAP*2);
        columnSlider.setBackground(Color.decode("#1c1617"));
        columnSlider.addChangeListener((ChangeListener) listener);
        columnSlider.setName("slots");
        
        addButton = new GridButton((ActionListener) listener, "addSlot", "+");
        
        imageButton.setSize(WIDTH/5*3, HEIGHT);
        imageButton.setLayout(null);
        imageButton.setName("image");
        imageButton.addActionListener((ActionListener) listener);        
        imageButton.add(backButton);
        
        this.add(sortByLabel);
        this.add(columnLabel);
        this.add(imageButton);
        this.add(locationTitle);
        this.add(sortBox);
        this.add(columnSlider);
        this.add(scrollable);
    }
    
    public void refresh(EventListener listener, Inventory inv, String loc) {
        String path = String.format("%s/%s/%s.png", GUI.FOLDER, "Locations", loc); 
        ImageIcon image = new ImageIcon(path);
        Image scaledImage = image.getImage().getScaledInstance(WIDTH/5*3, HEIGHT, Image.SCALE_SMOOTH);
        imageButton.setIcon(new ImageIcon(scaledImage));
        
        locationTitle.setText(loc);
        
        columnSlider.removeChangeListener((ChangeListener) listener);
        columnSlider.setValue(inv.getLocationColumns(loc));
        columnSlider.addChangeListener((ChangeListener) listener);
        
        // force no listener
        sortBox.removeActionListener((ActionListener) listener);
        sortBox.setSelectedIndex(Arrays.asList(GUI.SORT_METHODS).indexOf(inv.getSort(loc)));
        sortBox.addActionListener((ActionListener) listener);
        
        slotsPanel.removeAll();
        for (int i = 0; i < inv.getNumSlots(loc); i++) {
            int numItems = inv.getSlotItemsCount(loc, i);
            int numTotalItems = inv.getSlotTotalItemsCount(loc, i);
            
            // accepts html for line break
            String text = "<html>Slot: %d<br>Item count: %d<br>Total item count: %d</html>".formatted(i, numItems, numTotalItems);
            String name = "slot|%d|%d|%d".formatted(i, numItems, numTotalItems);
            GridButton slotButton = new GridButton((ActionListener) listener, name, text);
            slotsPanel.add(slotButton);
        }       
        
        update(inv, loc);
    }
    
    public void update(Inventory inv, String loc) {
        int columns = inv.getLocationColumns(loc);
        layout.setColumns(columns);
        
        int numSlots = inv.getNumSlots(loc);
        GridButton[] buttons = new GridButton[numSlots];
        
        for (int i = 0; i < numSlots; i++) { // ignores add button
            buttons[i] = (GridButton) slotsPanel.getComponent(i);
        }
        slotsPanel.removeAll();
        
        List<String> sortMethods = Arrays.asList(GUI.SORT_METHODS);
        Comparator<GridButton> comparator = Comparator.comparingInt(button -> {
            int sort = sortMethods.indexOf(inv.getLocationSort(loc)) + 1;// first is "slot"
            String value = button.getButtonActionCommand().split("\\|")[sort];
            
            return Integer.parseInt(value);
        });
        Arrays.sort(buttons, comparator);
        
        int size = (WIDTH/5*2-GUI.GAP*3-GUI.GAP*(columns-1))/columns;
        for (GridButton slotButton: buttons) {
            slotButton.setButtonSize(size);
            slotsPanel.add(slotButton);
        }
        
        // last
        addButton.setButtonSize(size);
        slotsPanel.add(addButton);
        
        // refresh
        slotsPanel.revalidate();
        slotsPanel.repaint();
    }
}
