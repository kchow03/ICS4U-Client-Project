package com.inventory;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import org.json.JSONArray;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Inventory {
    public static final String FOLDER = "Inventory/";
    public static final String PATH = FOLDER + "Inventory.json";
    public static final String ITEMS_PATH = FOLDER + "ItemList.json";
    private JSONObject inv;
    private JSONObject items;
    
    public Inventory() {
        // check if folder exists
        File folder = new File(FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        // load db files
        inv = load(PATH);
        items = load(ITEMS_PATH);
    }
    
    private JSONObject getLocation(String loc) {
        return inv.getJSONObject(loc);
    }
    
    private JSONObject getSlot(String loc, int index) {
        JSONObject location = this.getLocation(loc);
        return location.getJSONArray("slots").getJSONObject(index);
    }
    
    private JSONObject getItem(String loc, int index, String itemName) {
        JSONObject slot = this.getSlot(loc, index);
        return slot.getJSONObject(itemName);
    }
    
    public String getLocationSort(String loc) {
        JSONObject location = this.getLocation(loc);
        return location.getString("sort");
    }
    
    public void setLocationSort(String loc, String sort) {
        JSONObject location = this.getLocation(loc);
        location.put("sort", sort);
    }
    
    public int getLocationColumns(String loc) {
        JSONObject location = this.getLocation(loc);
        return location.getInt("columns");
    }
    
    public void setLocationColumns(String loc, int columns) {
        JSONObject location = this.getLocation(loc);
        location.put("columns", columns);
    }
        
    public int getNumSlots(String loc) {
        JSONObject location = this.getLocation(loc);
        return location.getJSONArray("slots").length();
    }
    
    public int getSlotItemsCount(String loc, int index) {
        JSONObject slot = this.getSlot(loc, index);
        return slot.length(); // returns num elements
    }
    
    public int getSlotTotalItemsCount(String loc, int index) {
        JSONObject slot = this.getSlot(loc, index);
        int total = 0;
        
        if (slot.length() == 0) {
            return 0;
        }
        
        for (Object itemName: slot.names()) { // null if empty
            JSONObject item = slot.getJSONObject((String) itemName); // object into string
            total += item.getInt("count");
        }
        
        return total;
    }
    
    public void addItem(String loc, int index, String itemName, int count) {
        JSONObject item = new JSONObject();
        item.put("count", count);
        
        JSONObject slot = this.getSlot(loc, index);
        slot.put(itemName, item);
    }
    
    public int getItemCount(String loc, int index, String itemName) { // cuh
        JSONObject item = this.getItem(loc, index, itemName);
        return item.getInt("count");
    }
    
    public boolean setItemCount(String loc, int index, String itemName, int count) {
        if (count >= 0) {
            JSONObject item = this.getItem(loc, index, itemName);
            item.put("count", count);
            return true;
        }
        return false;
    }
    
    public String[] getItems(String loc, int index) {
        JSONObject slot = this.getSlot(loc, index);
        String[] items = JSONObject.getNames(slot);
        
        if (items == null) {
            items = new String[0];
        }
        
        return items;
    }
    
    public String getSort(String loc) {
        JSONObject location = this.getLocation(loc);
        return location.getString("sort");
    }
    
    public String[] getLocations() {
        return JSONObject.getNames(inv);
    }
    
    public Rectangle getBounds(String loc) {
        JSONObject location = this.getLocation(loc);
        JSONArray bounds = location.getJSONArray("bounds");
        return new Rectangle(bounds.getInt(0), bounds.getInt(1), bounds.getInt(2), bounds.getInt(3));
    }
    
    public void save() {
        String[] paths = {PATH, ITEMS_PATH};
        for (String path: paths) {
            try (FileWriter file = new FileWriter(path);) {
                file.write(inv.toString(2));            
                file.close();
            } catch (IOException e) { // file doesn't exist
                new File(path); // create file
                save();
            }
        }
    }
    
    private static JSONObject load(String path) {
        try (FileReader file = new FileReader(path)) {
            JSONObject data = new JSONObject(new JSONTokener(file));
            file.close();
            
            return data;
        } catch (IOException e) { // file doesn't exist
            return new JSONObject();
        }
    }
}