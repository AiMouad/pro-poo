package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    @FXML
    private Rectangle rectangle;

    //1 Frame every 15 milliseconds, which means approximately 66.67 FPS
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(15), new EventHandler<ActionEvent>() {

        double deltaX = 2;
        double deltaY = 2;

        @Override
        public void handle(ActionEvent actionEvent) {
            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            Bounds bounds = scene.getBoundsInLocal();

            // Check for collision with scene bounds
            if (circle.getLayoutX() <= bounds.getMinX() || circle.getLayoutX() >= bounds.getMaxX() - circle.getRadius()) {
                deltaX *= -1; // Reverse direction for left or right collision
            }
            if (circle.getLayoutY() <= bounds.getMinY() || circle.getLayoutY() >= bounds.getMaxY() - circle.getRadius()) {
                deltaY *= -1; // Reverse direction for top or bottom collision
                if (circle.getLayoutY() <= bounds.getMinY() || circle.getLayoutY() >= bounds.getMaxY() - circle.getRadius()) {
                    System.err.println("Game Over!"); // If top or bottom collision, print "Game Over"
                }
            }

            // Check for collision with rectangle
            if (circle.getBoundsInParent().intersects(rectangle.getBoundsInParent())) {
                // If circle touches the rectangle, reverse direction
                deltaX *= -1;
                deltaY *= -1;
            }
        }
    }));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Add mouse event handler for moving the rectangle horizontally
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Set rectangle's layoutX to mouse's X position
                rectangle.setLayoutX(mouseEvent.getX() - rectangle.getWidth() / 2); // Center the rectangle under the mouse
            }
        });
    }
}
