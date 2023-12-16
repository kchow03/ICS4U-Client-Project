package com.inventory;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interactive Dark Mode GUI");

        // Create a root stack pane
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #333;"); // Set background color to dark gray

        // Create a button
        Button button = new Button("Click me!");
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Green button with white text

        // Add button to the root pane
        root.getChildren().add(button);

        // Add event handler for button click
        button.setOnAction(e -> animateButton(button));

        // Create a scene with the root pane
        Scene scene = new Scene(root, 400, 300);
        scene.setFill(Color.TRANSPARENT); // Make the scene background transparent

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void animateButton(Button button) {
        // Create a timeline for the animation
        Timeline timeline = new Timeline();

        // Add keyframes to change button color
        KeyValue keyValue1 = new KeyValue(button.styleProperty(), "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        KeyValue keyValue2 = new KeyValue(button.styleProperty(), "-fx-background-color: #2196F3; -fx-text-fill: white;");
        KeyValue keyValue3 = new KeyValue(button.styleProperty(), "-fx-background-color: #F44336; -fx-text-fill: white;");

        // Add keyframes to the timeline
        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, keyValue1);
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(300), keyValue2);
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(600), keyValue3);

        // Add keyframes to the timeline
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2, keyFrame3);

        // Play the timeline
        timeline.play();
    }
}