package com.inventory;

import java.io.*;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Inventory {
    public static final String PATH = "Inventory.json";
    public static final String ITEMS_PATH = "ItemList.json";
    private JSONObject inv;
    private JSONObject items;
    private Utility util;

    private String locName;
    private String itemName;

    private void itemMenu(JSONObject location) {
        String msg = String.format("%s\n\n", itemName);
        msg += String.format("Count: %d\n\n", location.getJSONObject(itemName).getInt("count"));
        msg += "[0] - Modify value\n";
        msg += "[1] - Remove item\n\n";
        msg += "[-1] - Exit\n\n";
        msg += "What would you like to do? ";

        int input = util.getInt(msg, -1, 1);

        if (input == -1) {
            return;
        } else if (input == 0) {
            int count = util.getInt("Count (-1 to exit): ", -1, 0); // no max
            
            if (count != -1 && count != 0) {
                location.getJSONObject(itemName).put("count", count);
                return;
            }
        }

        // else || count == 0
        location.remove(itemName); 
    }

    private String getType(String name) {
        String msg = String.format("%s - Type:\n", name);
        for (int i = 0; i < Expandable.types.length; i++) {
            msg += String.format("[%d] - %s\n", i, Expandable.types[i]);
        }
        msg += "\nWhat type is your expandable? ";

        int input = util.getInt(msg, 0, Expandable.types.length-1); // -1 because if its equal fail
        return Expandable.types[input];        
    }

    private String newItem() {
        Utility.clearScreen();
        System.out.print("Name: ");

        String name = util.getString();        

        if (items.has(name)) {
            util.invalid("Item already exists");
            return null;
        }

        JSONObject itemData = new JSONObject();
        String type = getType(name);
        itemData.put("type", type);

        items.put(name, itemData);
        save(ITEMS_PATH, items);

        return name;
    }

    private String addItem(JSONObject location) {
        int input;

        String msg = "Items:\n";
        String[] keys = JSONObject.getNames(items);
        for (int i = 0; i < items.length(); i++) {
            msg += "[%d] - %s\n".formatted(i+1, keys[i]);
        }
        msg += "\n[0] - Create new\n\n[-1] - Exit\n\nWhat would you like to add? ";

        input = util.getInt(msg, -1, items.length());
        
        String name;
        if (input == -1) {
            return "exit";
        } else if (input == 0) {
            name = newItem();
        } else {
            name = keys[input-1];
        }
        
        if (location.has(name)) {
            util.invalid("Item already in location");
            return null;
        }

        JSONObject itemData = new JSONObject();
        itemData.put("count", 0);
        location.put(name, itemData);

        return name;
    }

    private String getItem(JSONObject location) {
        String msg = String.format("%s - Items:\n", locName);
        int input;

        String[] keys = JSONObject.getNames(location);
        for (int i = 0; i < location.length(); i++) {
            int count = location.getJSONObject(keys[i]).getInt("count");

            msg += "[%d] - %s x %d\n".formatted(i+1, keys[i], count);
        }
        msg += "\n[0] - Add item\n\n";
        msg += "[-1] - Exit\n\n";
        msg += "What would you like to do? ";

        input = util.getInt(msg, -1, location.length()+1);


        if (input == -1) {
            return "exit";
        } else if (input == 0) {
            return addItem(location);
        } else {
            return keys[input-1];
        }
    }
    
    private String newLocation() {
        String name = null;

        Utility.clearScreen();
        System.out.print("New Location Name [-1 to cancel]: ");
        name = util.getString();

        if (inv.has(name)) { // already exists check
            util.invalid("Location already exists");
        } else if (!name.equals("-1")) { // not exit
            inv.put(name, new JSONObject());
            save(PATH, inv);
        }
        
        return name;
    }

    private String getLocation() {        
        String msg = "Locations:\n";
        int input;

        String[] keys = JSONObject.getNames(inv);

        for (int i = 0; i < inv.length(); i++) {
            msg += String.format("[%d] - %s\n", i+1, keys[i]);
        }

        msg += "\n[0] - Create new\n\n[-1] - Exit\n\nWhere would you like to go? ";
        
        input = util.getInt(msg, -1, inv.length());

        if (input == -1) {
            return "exit";
        } else if (input == 0) { // new location
            return newLocation();
        } else { // selected a location
            return keys[input-1];
        }
    }

    public Inventory() {
        inv = load(PATH); // load required objects
        items = load(ITEMS_PATH);
        util = new Utility();

        do {
            locName = getLocation();

            while (!itemName.equals("exit") && !locName.equals("exit")) {
                JSONObject location = inv.getJSONObject(locName);
                itemName = getItem(location);
                // if null then continue

                if (!itemName.equals("exit")) {
                    itemMenu(location);
                    save(PATH, inv);
                }
            };
        } while (!locName.equals("exit"));
    }

    public static void main(String[] args) {
        new Inventory();
    }

    private static JSONObject load(String path) {
        try (FileReader file = new FileReader(path)) {
            JSONObject data = new JSONObject(new JSONTokener(file));
            file.close();

            return data;
        } catch (IOException e) {// only happens if file doesnt exist
            save(path, new JSONObject()); // generate file
            return load(path); // retry
        }
    }

    private static void save(String path, JSONObject data) {
        try (PrintWriter file = new PrintWriter(new FileWriter(path))) {
            file.print(data.toString(2));
            file.close();
        } catch (IOException e) { // file doesn't exist
            new File(path); // generate file
        }
    }
}
