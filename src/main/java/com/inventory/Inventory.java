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
        
    public int getNumSlots(String key) {
        return inv.getJSONObject(key).getJSONArray("slots").length();
    }
    
    public int getItemCount(String location, int index, String item) { // cuh
        return inv.getJSONObject(location).getJSONArray("slots").getJSONObject(index).getJSONObject(item).getInt("count");
    }
    
    public String[] getItems(String location, int index) {
        String[] items = JSONObject.getNames(inv.getJSONObject(location).getJSONArray("slots").getJSONObject(index));
        
        if (items == null) {
            items = new String[0];
        }
        
        return items;
    }
    
    public String[] getLocations() {
        return JSONObject.getNames(inv);
    } 
    
    public Rectangle getBounds(String key) {
        JSONArray bounds = inv.getJSONObject(key).getJSONArray("bounds");
        return new Rectangle(bounds.getInt(0), bounds.getInt(1), bounds.getInt(2), bounds.getInt(3));
    }
    
    private static void save(String path, JSONObject data) {
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

//    public JSONObject getLoc(String loc) {
//        JSONObject data = inv.getJSONObject(loc);
//        return data;
//    }
//
//    public boolean isValidLoc(String loc) {
//        boolean data = inv.has(loc);
//        return data;
//    }
//
//    public boolean isValidItem(String item) {
//        boolean data = items.has(item);
//        return data;
//    }
//
//    public void changeVal(String loc, String item, int val) {
//        JSONObject locData = inv.getJSONObject(loc);
//        int count = locData.getInt(item);
//        locData.put(item, count+val);
//
//        save(PATH, inv);
//    }
//
//
//    public String getInv() {
//        return inv.toString(2);
//    }
}
