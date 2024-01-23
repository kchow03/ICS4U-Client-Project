package com.inventory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Inventory {
    public static final String PATH = Handler.FOLDER + "Inventory.json";
    public static final String ITEMS_PATH = Handler.FOLDER + "ItemList.json";
    private String loc;
    private int index;
    private String itemName;
    private JSONObject inv;
    private JSONObject items;
    
    public Inventory() {        
        // load db files
        inv = load(PATH);
        items = load(ITEMS_PATH);
    }
    
    // get items
    public String[] getItemsList() {
        return JSONObject.getNames(items);
    }
    public boolean hasItemList(String itemName) {
        return items.has(itemName);
    }
    public void setItemList(String itemName) {
        items.put(itemName, new JSONObject());
    }
    
    public boolean hasItem(String itemName) {
        JSONObject slot = getSlot();
        return slot.has(itemName);
    }
    
    // get current
    public String getLocationName() {
        return loc;
    }
    
    public int getSlotIndex() {
        return index;
    }
    
    public String getItemName () {
        return itemName;
    }
    
    // get JSONObject
    private JSONObject getLocation() {
        return inv.getJSONObject(loc);
    }
    
    private JSONObject getSlot() {
        JSONObject location = getLocation();
        return location.getJSONArray("slots").getJSONObject(index);
    }
    
    private JSONObject getItem() {
        JSONObject slot = getSlot();
        return slot.getJSONObject(itemName);
    }
    
    // set current
    public void setLocation(String loc) {
        this.loc = loc;
    }
    public void setSlot(int index) {
        this.index = index;
    }
    public void setItem(String itemName) {
        this.itemName = itemName;
    }
    
    // check
    public boolean hasLocation() {
        return inv.has(loc);
    }
    
    // getLocation
    public void setLocation(ArrayList<int[]> points) {
        JSONObject data = new JSONObject();
        data.put("slots", new JSONArray());
        data.put("columns", 3);
        data.put("sort", "Slot");
        data.put("points", points);
        
        inv.put(loc, data);
    }
    public void remLocation() {
        inv.remove(loc);
    }
    
    public String getLocationSort() {
        JSONObject location = this.getLocation();
        return location.getString("sort");
    }
    
    public void setLocationSort(String sort) {
        JSONObject location = this.getLocation();
        location.put("sort", sort);
    }
    
    public int getLocationColumns() {
        JSONObject location = this.getLocation();
        return location.getInt("columns");
    }
    
    public void setLocationColumns(int columns) {
        JSONObject location = this.getLocation();
        location.put("columns", columns);
    }
        
    public int getNumSlots() {
        JSONObject location = this.getLocation();
        return location.getJSONArray("slots").length();
    }
    
    public int getSlotItemsCount() {
        JSONObject slot = this.getSlot();
        return slot.length(); // returns num elements
    }
    
    public int getSlotTotalItemsCount() {
        JSONObject slot = this.getSlot();
        int total = 0;
        
        if (slot.length() == 0) {
            return 0;
        }
        
        for (Object item: slot.names()) { // null if empty
            JSONObject itemData = slot.getJSONObject((String) item); // object into string
            total += itemData.getInt("count");
        }
        
        return total;
    }
    
    public void addSlot() {
        JSONObject location = getLocation();
        JSONArray slots = location.getJSONArray("slots");
        
        JSONObject data = new JSONObject();
        slots.put(data);
    }
    
    public void remSlot() {
        JSONObject location = getLocation();
        JSONArray slots = location.getJSONArray("slots");
        
        slots.remove(index);
    }
    
    public void addItem(int count) {
        JSONObject item = new JSONObject();
        item.put("count", count);
        
        JSONObject slot = this.getSlot();
        slot.put(itemName, item);
    }
    
    public int getItemCount() { // cuh
        JSONObject item = this.getItem();
        return item.getInt("count");
    }
    
    public boolean setItemCount(int count) {
        if (count >= 0) {
            JSONObject item = this.getItem();
            item.put("count", count);
            return true;
        }
        return false;
    }
    
    public String[] getItems() {
        JSONObject slot = this.getSlot();
        String[] itemsList = JSONObject.getNames(slot);
        
        if (itemsList == null) {
            itemsList = new String[0];
        }
        
        return itemsList;
    }
    
    public String getSort() {
        JSONObject location = this.getLocation();
        return location.getString("sort");
    }
    
    public ArrayList<String> getLocations() {
        String[] locations = inv.isEmpty() ? new String[0] : JSONObject.getNames(inv);
        ArrayList<String> list = new ArrayList(Arrays.asList(locations));
        
        return list;
    }
    
    public int[][] getLocationPoints() {
        int[][] points = new int[4][2];
        
        JSONObject location = this.getLocation();
        
        JSONArray pointArrays = location.getJSONArray("points"); // array of arrays
        for (int i = 0; i < 4; i++) {
            JSONArray point = pointArrays.getJSONArray(i);
            points[i][0] = point.getInt(0);
            points[i][1] = point.getInt(1);
        }
        
        return points;
    }
    
    public void save() {
        // cleanup inv
        for (String loc: getLocations()) {
            setLocation(loc);
            for (int i = 0; i < getNumSlots(); i++) {
                setSlot(i);
                // removes slot if empty
                if (getSlotTotalItemsCount() == 0) remSlot();
            }   
        }
        
        
        
        String[] paths = {PATH, ITEMS_PATH};
        JSONObject[] datas = {inv, items};
        
        for (int i = 0; i < paths.length; i++) {
            try (FileWriter file = new FileWriter(paths[i]);) {
                file.write(datas[i].toString(4));            
                file.close();
            } catch (IOException e) { // file doesn't exist
                new File(paths[i]); // create file
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