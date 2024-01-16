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
    
    public int getLocationColumns(String loc) {
        JSONObject location = this.getLocation(loc);
        return location.getInt("columns");
    }
        
    public int getNumSlots(String loc) {
        JSONObject location = this.getLocation(loc);
        return location.getJSONArray("slots").length();
    }
    
    public int getNumItems(String loc, int index) {
        JSONObject slot = this.getSlot(loc, index);
        
        return slot.names().length(); // returns num elements
    }
    
    public int getNumTotalItems(String loc, int index) {
        JSONObject slot = this.getSlot(loc, index);
        int total = 0;
        
        for (Object itemName: slot.names()) {
            JSONObject item = slot.getJSONObject((String) itemName); // object into string
            total += item.getInt("count");
        }
        
        return total;
    }
    
    public int getItemCount(String loc, int index, String itemName) { // cuh
        JSONObject item = this.getItem(loc, index, itemName);
        return item.getInt("count");
    }
    
    public boolean setItemCount(String loc, int index, String itemName, int count) {
        if (count >= 0) {
            JSONObject item = this.getItem(loc, index, itemName);
            item.put("count", count);
            save(PATH, inv);
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
    
    public void setNumColumns(String loc, int numColumns) {
        JSONObject location = this.getLocation(loc);
        location.put("columns", numColumns);
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
        try {
            FileWriter file = new FileWriter(PATH);
            FileWriter items_file = new FileWriter(ITEMS_PATH);
            
            file.write(inv.toString(2));
            items_file.write(items.toString(2));
            
            System.out.println(inv.toString(2));
            
            file.close();
            items_file.close();
        } catch (IOException e) {
            new File(PATH); // create file
            new File(ITEMS_PATH); // create items path
            save(); // retry
        }
    }
    
    public static void save(String path, JSONObject data) {
        String jsonString = data.toString(2);
        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonString);
            file.close();
        } catch (IOException e) { // file doesn't exist
            new File(path); // generate file
            save(path, data); // retry
        }
    }
    
    private static JSONObject load(String path) {
        try (FileReader file = new FileReader(path)) {
            JSONObject data = new JSONObject(new JSONTokener(file));
            file.close();
            
            return data;
        } catch (IOException e) { // file doesn't exist
            save(path, new JSONObject()); // generate file
            return load(path); // retry
        }
    }
}