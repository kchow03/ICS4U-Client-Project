package com.inventory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.event.ChangeEvent;

import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
public class SlotsPanel extends JPanel implements ActionListener, ChangeListener {
    static final String[] SORT_METHODS = {"Slot", "Number of items", "Total number of items"};
    Inventory inv;
    EventListener listener;
    JButton imageButton;
    JLabel locationTitle;
    JPanel slotsPanel;
    JComboBox sortBox;
    JSlider columnSlider;
    JButton addSlotButton;
    
    public SlotsPanel(EventListener listener, Inventory inv) {
        this.listener = listener;
        this.inv = inv;
        setLayout(new BorderLayout());
        
        addSlotButton = new JButton("+");
        addSlotButton.setActionCommand("addSlot");
        addSlotButton.addActionListener(this);
        
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("locations");
        backButton.setOpaque(false);
        backButton.setPreferredSize(new Dimension(0, 33));
        backButton.addActionListener((ActionListener) listener);
        
        imageButton = new JButton("No image preview");
        imageButton.setActionCommand("setSlotImage");
        imageButton.addActionListener(this);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Handler.BACKGROUND);
        contentPanel.setPreferredSize(new Dimension(310, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        locationTitle = new JLabel("Placeholder", SwingConstants.CENTER);
        locationTitle.setForeground(Handler.FOREGROUND);
        locationTitle.setPreferredSize(new Dimension(0, 66));
        
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(Handler.SECONDARY);
        
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        optionsPanel.setPreferredSize(new Dimension(0, 50));
        
        JPanel sortPanel = new JPanel(new BorderLayout());
        sortPanel.setOpaque(false);
        JLabel sortByLabel = new JLabel("Sort by: ");
        sortBox = new JComboBox(SORT_METHODS);
        sortBox.setActionCommand("setSort");
        sortPanel.add(sortByLabel, BorderLayout.WEST);
        sortPanel.add(sortBox, BorderLayout.CENTER);
        
        JPanel columnPanel = new JPanel(new BorderLayout());
        columnPanel.setOpaque(false);
        JLabel columnLabel = new JLabel("Columns: ");
        columnSlider = new JSlider(1, 5);
        columnPanel.add(columnLabel, BorderLayout.WEST);
        columnPanel.add(columnSlider, BorderLayout.CENTER);
        
        // slots
        slotsPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        slotsPanel.setBackground(Handler.SECONDARY);
        JScrollPane scrollable = new JScrollPane(
            slotsPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
//        
        
        
        optionsPanel.add(sortPanel);
        optionsPanel.add(columnPanel);
        controlPanel.add(optionsPanel, BorderLayout.NORTH);
        controlPanel.add(scrollable, BorderLayout.CENTER);
        controlPanel.add(backButton, BorderLayout.SOUTH);
        
        contentPanel.add(locationTitle, BorderLayout.NORTH);
        contentPanel.add(controlPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.EAST);
        add(imageButton, BorderLayout.CENTER);
    }
    
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (!aFlag) return;
        
        String loc = inv.getLocationName();
        
        
        String path = String.format("%s/%s/%s.png", Handler.FOLDER, "Locations", loc); 
        Image image = new ImageIcon(path).getImage();
        
        Image scaledImage = image.getScaledInstance(imageButton.getWidth(), imageButton.getHeight(), Image.SCALE_SMOOTH);
        imageButton.setIcon(new ImageIcon(scaledImage));
        
        locationTitle.setText(loc);
        
        // force no listener
        sortBox.removeActionListener(this);
        columnSlider.removeChangeListener(this);
        sortBox.setSelectedIndex(Arrays.asList(SORT_METHODS).indexOf(inv.getSort()));
        columnSlider.setValue(inv.getLocationColumns());
        columnSlider.addChangeListener(this);
        sortBox.addActionListener(this);
        
        slotsPanel.removeAll(); // reset
        for (int i = 0; i < inv.getNumSlots(); i++) {
            inv.setSlot(i);
            int numItems = inv.getSlotItemsCount();
            int numTotalItems = inv.getSlotTotalItemsCount();
            
//             accepts html for line break
            JButton button = new JButton();
            button.setText("<html>Slot: %d<br>Item count: %d<br>Total item count: %d</html>".formatted(i, numItems, numTotalItems));
            button.setActionCommand("items|%d|%d|%d".formatted(i, numItems, numTotalItems));
            button.setOpaque(false);
            button.addActionListener((ActionListener) listener);
            slotsPanel.add(button);
        }
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        GridLayout gl = (GridLayout) slotsPanel.getLayout();
        gl.setColumns(inv.getLocationColumns());
        
        slotsPanel.remove(addSlotButton);
        ArrayList<JButton> buttons = new ArrayList<>();
        for (Component button: slotsPanel.getComponents()) {
            buttons.add((JButton) button);
        }
        slotsPanel.removeAll();
        
        java.util.List<String> sorts = Arrays.asList(SORT_METHODS);
        Collections.sort(buttons, Comparator.comparingInt(button -> {
            int sort = sorts.indexOf(inv.getLocationSort()) + 1; // frist is slot
            String value = button.getActionCommand().split("\\|")[sort];
            
            return Integer.parseInt(value);
        }));
        for (JButton button: buttons) {
            slotsPanel.add(button);
        }
        // last
        slotsPanel.add(addSlotButton);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        
        switch (action) {
            case "setSlotImage" -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setAcceptAllFileFilterUsed(false);

                chooser.setDialogTitle("Select an image for the location preview");

                FileNameExtensionFilter restriction = new FileNameExtensionFilter("Select a .png file", "png");
                chooser.addChoosableFileFilter(restriction);

                int r = chooser.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    Path source = new File(chooser.getSelectedFile().getAbsolutePath()).toPath();
                    Path destination = new File("%s/%s/%s.png".formatted(Handler.FOLDER, "Location", inv.getLocationName())).toPath();
                    
                    try {
                        Files.copy(source, destination);
                        setVisible(true); // reload
                    } catch (IOException exception) {
                        imageButton.setText("An error has occured");
                        System.out.println(exception);
                    }
                    
                    

                    // set the label to the path of the selected file
    //                    l.setText(j.getSelectedFile().getAbsolutePath());
                }
            } case "setSort" -> {
                String value = (String) ((JComboBox) e.getSource()).getSelectedItem();
                inv.setLocationSort(value);
                repaint();
                revalidate();
            } 
        }
    };
    
    @Override
    public void stateChanged(ChangeEvent e) { // sort
        int value = ((JSlider) e.getSource()).getValue();
        inv.setLocationColumns(value);
        repaint();
        slotsPanel.revalidate();
    };
}
