/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.test3d;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.vecmath.Color3f;

public class SphereExample {

    public SphereExample() {
        // Create a simple universe with default values
        SimpleUniverse universe = new SimpleUniverse();

        // Create a branch group
        BranchGroup branchGroup = createSceneGraph();

        // Set the branch group as the root of the universe
        universe.addBranchGraph(branchGroup);
    }

    private BranchGroup createSceneGraph() {
        // Create a branch group
        BranchGroup branchGroup = new BranchGroup();

        // Create a sphere with radius 0.5
        Sphere sphere = new Sphere(0.5f);

        // Create an appearance for the sphere
        Appearance appearance = new Appearance();

        // Set sphere color to blue
        Color3f color = new Color3f(0.0f, 0.0f, 1.0f);
        ColoringAttributes coloringAttributes = new ColoringAttributes(color, ColoringAttributes.SHADE_GOURAUD);
        appearance.setColoringAttributes(coloringAttributes);

        // Set polygon attributes to fill
        PolygonAttributes polyAttributes = new PolygonAttributes();
        polyAttributes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        appearance.setPolygonAttributes(polyAttributes);

        // Set the appearance for the sphere
        sphere.setAppearance(appearance);

        // Add the sphere to the branch group
        branchGroup.addChild(sphere);

        return branchGroup;
    }

    public static void main(String[] args) {
        // Create an instance of SphereExample
        new SphereExample();
    }
}