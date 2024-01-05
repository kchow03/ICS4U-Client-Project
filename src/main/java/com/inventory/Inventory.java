package com.inventory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Inventory {
    public static final String FOLDER = "Inventory/";
    public static final String PATH = "Inventory.json";
    public static final String ITEMS_PATH = "ItemList.json";
    private JSONObject inv;
    private JSONObject items;

    public Inventory() {
        checkFolder();

        inv = load(PATH); // load required objects
        items = load(ITEMS_PATH);
    }

    public JSONObject getLoc(String loc) {
        JSONObject data = inv.getJSONObject(loc);
        return data;
    }

    public boolean isValidLoc(String loc) {
        boolean data = inv.has(loc);
        return data;
    }

    public boolean isValidItem(String item) {
        boolean data = items.has(item);
        return data;
    }

    public void changeVal(String loc, String item, int val) {
        JSONObject locData = inv.getJSONObject(loc);
        int count = locData.getInt(item);
        locData.put(item, count+val);

        save(PATH, inv);
    }

    private static void checkFolder() {
        File dir = new File(FOLDER);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    
    private static JSONObject load(String path) {
        try (FileReader file = new FileReader(FOLDER + path)) {
            JSONObject data = new JSONObject(new JSONTokener(file));
            file.close();

            return data;
        } catch (IOException e) {// only happens if file doesnt exist
            save(path, new JSONObject()); // generate file
            return load(path); // retry
        }
    }

    private static void save(String path, JSONObject data) {
        try (PrintWriter file = new PrintWriter(new FileWriter(FOLDER + path))) {
            file.print(data.toString(2));
            file.close();
        } catch (IOException e) { // file doesn't exist
            new File(FOLDER + path); // generate file
        }
    }

    public String getInv() {
        return inv.toString(2);
    }
}
