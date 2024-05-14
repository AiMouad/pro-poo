package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label scoreLabel;

    private int score = 0;
    private int scoreCount = 0;
    private boolean gameOver = false;
    private double deltaX;
    private double deltaY;

    Timeline timeline;
    RotateTransition rotateTransition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deltaX = -2;
        deltaY = 2;

        timeline = new Timeline(new KeyFrame(Duration.millis(15), event -> {
            if (!gameOver) {
                circle.setLayoutX(circle.getLayoutX() + deltaX);
                circle.setLayoutY(circle.getLayoutY() + deltaY);

                Bounds bounds = scene.getBoundsInLocal();

                if (circle.getLayoutX() <= bounds.getMinX() + circle.getRadius()
                        || circle.getLayoutX() >= bounds.getMaxX() - circle.getRadius()) {
                    deltaX *= -1;
                }

                if (circle.getLayoutY() <= bounds.getMinY() + circle.getRadius()) {
                    deltaY *= -1;
                }

                if (circle.getLayoutY() >= bounds.getMaxY() - circle.getRadius()) {
                    gameOver = true;
                    System.err.println("Game Over! Final Score: " + score);
                    scoreLabel.setText("Score: " + score);
                    stopRotation(rotateTransition);
                }

                if (circle.getBoundsInParent().intersects(rectangle.getBoundsInParent())) {
                    deltaY *= -1;
                    score++;
                    scoreCount++;
                    System.out.println("Score: " + score);

                    if (scoreCount % 2 == 0) {
                        deltaX *= 1.1;
                        deltaY *= 1.1;
                    }
                }
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Image image = new Image(getClass().getResourceAsStream("/sample/rm251-mind-01-a.jpg"));
        imageView.setImage(image);

        ImagePattern imagePattern = new ImagePattern(new Image(getClass().getResourceAsStream("/sample/sphere_6181270.png")));
        circle.setFill(imagePattern);

        rotateTransition = new RotateTransition(Duration.seconds(2), circle);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.play();

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rectangle.setLayoutX(mouseEvent.getX() - rectangle.getWidth() / 2);
            }
        });

        restartButton.setOnAction(event -> restartGame());
    }

    private void restartGame() {
        score = 0;
        scoreCount = 0;
        gameOver = false;
    
        // Reset ball velocity
        deltaX = -2;
        deltaY = 2;
    
        timeline.stop(); // Stop the timeline
        timeline.getKeyFrames().clear(); // Clear existing key frames
        // Add a new key frame with the updated duration
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(15), event -> {
            if (!gameOver) {
                circle.setLayoutX(circle.getLayoutX() + deltaX);
                circle.setLayoutY(circle.getLayoutY() + deltaY);
    
                Bounds bounds = scene.getBoundsInLocal();
    
                if (circle.getLayoutX() <= bounds.getMinX() + circle.getRadius()
                        || circle.getLayoutX() >= bounds.getMaxX() - circle.getRadius()) {
                    deltaX *= -1;
                }
    
                if (circle.getLayoutY() <= bounds.getMinY() + circle.getRadius()) {
                    deltaY *= -1;
                }
    
                if (circle.getLayoutY() >= bounds.getMaxY() - circle.getRadius()) {
                    gameOver = true;
                    System.err.println("Game Over! Final Score: " + score);
                    stopRotation(rotateTransition);
                }
                
    
                if (circle.getBoundsInParent().intersects(rectangle.getBoundsInParent())) {
                    deltaY *= -1;
                    score++;
                    scoreCount++;
                    System.out.println("Score: " + score);
    
                    if (scoreCount % 2 == 0) {
                        deltaX *= 1.1;
                        deltaY *= 1.1;
                    }
                }
            }
        }));
        
        timeline.play(); // Restart the timeline with the updated duration
        circle.setLayoutX(150);
        circle.setLayoutY(20);
    }
    

    private void stopRotation(RotateTransition rotateTransition) {
        if (gameOver) {
            rotateTransition.stop();
        }
    }

    
}
