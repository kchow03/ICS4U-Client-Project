package com.inventory;

public class Expandable {
    public static final String types[] = {"Tool", "Material"};
    private final String name;
    private int count;
    private String location;

    public Expandable() { // not sure
        this(null, 0, null);
    }

    public Expandable(String n, int c, String l) {
        name = n;
        count = c;
        location = l;
    }

    public String getName() {
        return this.name;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
