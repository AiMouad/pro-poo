package sample;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller implements Initializable {

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    @FXML
    private Rectangle rectangle;

    @FXML
    private Button restartButton;

    @FXML
    private ImageView imageView;

    private int score = 0;
    private boolean gameOver = false;

    // 1 Frame every 15 milliseconds, which means approximately 66.67 FPS
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(15), new EventHandler<ActionEvent>() {

        double deltaX = -2;
        double deltaY = 2;

        @Override
        public void handle(ActionEvent actionEvent) {
            if (!gameOver) {
                circle.setLayoutX(circle.getLayoutX() + deltaX);
                circle.setLayoutY(circle.getLayoutY() + deltaY);

                Bounds bounds = scene.getBoundsInLocal();

                // Check for collision with scene bounds
                if (circle.getLayoutX() <= bounds.getMinX() + circle.getRadius() // Adjusted condition
                        || circle.getLayoutX() >= bounds.getMaxX() - circle.getRadius()) {
                    deltaX *= -1; // Reverse direction for left or right collision
                }

                if (circle.getLayoutY() <= bounds.getMinY() + circle.getRadius()) {
                    deltaY *= -1; // Reverse direction for top collision
                }

                if (circle.getLayoutY() >= bounds.getMaxY() - circle.getRadius()) {
                    gameOver = true;
                    System.err.println("Game Over! Final Score: " + score);
                    stopRotation(rotateTransition); // Stop the rotation
                }

                // Check for collision with rectangle
                if (circle.getBoundsInParent().intersects(rectangle.getBoundsInParent())) {
                    // If circle touches the rectangle, reverse direction
                    // deltaX *= -1;
                    deltaY *= -1;
                    // Increment score
                    score++;
                    System.out.println("Score: " + score);
                }
            }
        }
    }));

    private RotateTransition rotateTransition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Image image = new Image(getClass().getResourceAsStream("/sample/rm251-mind-01-a.jpg"));
        imageView.setImage(image);

        ImagePattern imagePattern = new ImagePattern(new Image(getClass().getResourceAsStream("/sample/sphere_6181270.png")));
        circle.setFill(imagePattern);

        rotateTransition = new RotateTransition(Duration.seconds(2), circle);
        rotateTransition.setByAngle(360); // Rotate by 360 degrees
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Repeat indefinitely
        rotateTransition.play(); // Start the animation

        

        // Add mouse event handler for moving the rectangle horizontally
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Set rectangle's layoutX to mouse's X position
                rectangle.setLayoutX(mouseEvent.getX() - rectangle.getWidth() / 2); // Center the rectangle under the mouse
            }
        });

        

        restartButton.setOnAction(event -> restartGame());
    }

    private void restartGame() {
        score = 0;
        gameOver = false;
        // Reset game state, e.g., reset circle position
        circle.setLayoutX(150);
        circle.setLayoutY(20);
        // You can add more reset logic here
    }

    private void stopRotation(RotateTransition rotateTransition) {
        if (gameOver) {
            rotateTransition.stop();
        }
    }
}
