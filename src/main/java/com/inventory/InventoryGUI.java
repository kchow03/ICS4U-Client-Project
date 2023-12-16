package com.inventory;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class InventoryGUI extends Application {

    private Inventory inventory;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory GUI");

        // Initialize Inventory
        inventory = new Inventory();

        // Create a root stack pane
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #333;"); // Set background color to dark gray

        // Create a button
        Button button = new Button("Open Inventory");
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Green button with white text

        // Add button to the root pane
        root.getChildren().add(button);

        // Configure layout
        StackPane.setAlignment(button, Pos.CENTER);

        // Add event handler for button click
        button.setOnAction(e -> showInventory());

        // Create a scene with the root pane
        Scene scene = new Scene(root, 400, 300);
        scene.setFill(Color.TRANSPARENT); // Make the scene background transparent

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void showInventory() {
        // Create a new stage for the Inventory GUI
        Stage inventoryStage = new Stage();
        inventoryStage.setTitle("Inventory System");

        // Create a root stack pane for the Inventory GUI
        StackPane inventoryRoot = new StackPane();
        inventoryRoot.setStyle("-fx-background-color: #333;"); // Set background color to dark gray

        // Create a label to display the inventory content
        Label inventoryLabel = new Label();
        inventoryLabel.setTextFill(Color.WHITE);

        // Add label to the inventory root pane
        inventoryRoot.getChildren().add(inventoryLabel);

        // Configure layout
        StackPane.setAlignment(inventoryLabel, Pos.CENTER);

        // Set the content of the label with the inventory data
        inventoryLabel.setText(inventory.getInv());

        // Create a scene with the inventory root pane
        Scene inventoryScene = new Scene(inventoryRoot, 600, 400);
        inventoryScene.setFill(Color.TRANSPARENT); // Make the scene background transparent

        // Set the scene to the inventory stage
        inventoryStage.setScene(inventoryScene);

        // Show the inventory stage
        inventoryStage.show();
    }
}
