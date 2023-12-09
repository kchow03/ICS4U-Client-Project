/*
 * Java JSON
 * https://www.geeksforgeeks.org/working-with-json-data-in-java/
 */

import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Inventory {
    public static final String PATH = "Inventory.json";
    public static final String ITEMS_PATH = "ItemList.json";
    private static JSONObject inv;

    public Inventory() {
        Utility util = new Utility();
        String message, name;

        inv = load(); // load on startup
        
        int input, itemInput;
        while (true) {
            message = "Locations:\n";
            for (int i = 0; i < inv.size(); i++) {
                message += "[%d] - %s\n".formatted(i+1, inv.keySet().toArray()[i]);
            }
            message += "\n[0] - Create new\n\n[-1] - Exit\n\nWhere would you like to go? ";

            do {
                input = util.getInt(message);
            } while (input < -1 || input > inv.size());

            if (input == -1) {
                break;
            } else if (input == 0) {
                Utility.clearScreen();
                System.out.print("New Location Name [-1 to cancel]: ");
                name = util.getString();

                if (name.equals("-1")) continue; // return
                if (inv.containsKey(name)) {
                    util.invalid("Already a location");
                    continue;
                }

                // make sure to include check that it doesn't already exist
                inv.put(name, new JSONObject());
                save();

                input = inv.size()+1;
            } else {
                name = (String) inv.keySet().toArray()[input-1];
            }

            // inventory system
            // get jsonobject from value array input
            JSONObject selected = (JSONObject) inv.get(name); // always jsonobject

            while (true) {
                message = "Items:\n";
                for (int i = 0; i < selected.size(); i++) {
                    String expandable = (String) selected.keySet().toArray()[i];
                    Object count = ((JSONObject) selected.values().toArray()[i]).get("count");

                    message += "[%d] - %s x %d\n".formatted(i+1, expandable, count);
                }
                message += """
                    \n[0] - Add item\n
                    [-1] - Exit\n
                    What would you like to do? 
                    """;

                do {
                    itemInput = util.getInt(message);
                } while (itemInput < -1 || itemInput >= selected.size()+1);
                
                if (itemInput == -1) { // break
                    break;
                }

                if (itemInput == 0) { // add
                    JSONObject items = loadItems();
                    message = "Items:\n";
                    for (int i = 0; i < items.size(); i++) {
                        message += "[%d] - %s\n".formatted(i+1, items.keySet().toArray()[i]);
                    }
                    message += "\n[0] - Create new\n\n[-1] - Exit\n\nWhat would you like to add? ";

                    do {
                        itemInput = util.getInt(message);
                    } while (itemInput < -1 || itemInput > items.size());
                    
                    if (itemInput == -1) {
                        break;
                    } else if (itemInput == 0) {
                        Utility.clearScreen();
                        System.out.print("Name: ");
                        name = util.getString();
                        
                        if (items.containsKey(name)) {
                            util.invalid();
                            continue;
                        }

                        message = "Type:\n";
                        for (int i = 0; i < Expandable.types.length; i++) {
                            message += "[%d] - %s\n".formatted(i, Expandable.types[i]);
                        }
                        message += "\nWhat type is your expandable? ";

                        do {
                            itemInput = util.getInt(message);
                        } while (itemInput < 0 || itemInput >= Expandable.types.length);

                        JSONObject itemData = new JSONObject();
                        itemData.put("type", Expandable.types[itemInput]);

                        items.put(name, itemData);
                        saveItems(items);

                        itemInput = items.size()+1;
                    } else {
                        name = (String) items.keySet().toArray()[itemInput-1];
                        if (selected.containsKey(name)) {
                            util.invalid("Item already in location");
                            continue;
                        }
                    }

                    JSONObject itemData = new JSONObject();
                    itemData.put("count", 0);

                    selected.put(name, itemData);
                } else {
                    name = (String) selected.keySet().toArray()[itemInput-1];
                }
                
                message = """
                            %s
                            Count: %d\n
                            [0] - Modify value
                            [1] - Remove item\n
                            [-1] - Exit\n\n
                            """.formatted(
                                name,
                                ((JSONObject) selected.get(name)).get("count")
                            );
                message += "What would you like to do? ";
                do {
                    input = util.getInt(message);
                } while (input < -1 || input > 1);

                if (input == 0) {
                    do {
                        input = util.getInt("Count (-1 to exit): ");
                    } while (input < -1);
                    
                    if (input == 0) {
                        selected.remove(name);
                    } else if (input != -1) {
                        ((JSONObject) selected.get(name)).replace("count", input);
                    }
                    save();
                } else if (input == 1) {
                    selected.remove(name);
                }            
            }
        }
    }

    private static JSONObject load() {
        try (FileReader file = new FileReader(PATH)) {
            return (JSONObject) JSONValue.parse(file);
        } catch (IOException e) {
            // only happens if file doesnt exist
            return null;
        }
    }

    private static void save() {
        try (PrintWriter file = new PrintWriter(new FileWriter(PATH))) {
            file.print(inv.toJSONString());
            file.close();
        } catch (IOException e) {
            // only happens if file doesn't exist
        }
    }

    private static JSONObject loadItems() {
        try (BufferedReader file = new BufferedReader(new FileReader(ITEMS_PATH))) {
            return (JSONObject) JSONValue.parse(file);
        } catch (IOException e) {
            return new JSONObject();
            // file doesn't exist
        }
    }

    public static void saveItems(JSONObject data) {
        try (PrintWriter file = new PrintWriter(new FileWriter(ITEMS_PATH))) {
            file.print(data);
        } catch (IOException e) {
            // file doesn't exist
        }
    }

    public static void main(String[] args) {
        new Inventory();
    }
}
