package com.inventory;

import com.groupdocs.viewer.Viewer;

public class Main {
    public static void main(String[] args) {
        Viewer viewer = new Viewer("bathroom-design.dwg");
        viewer.close();
    }
}