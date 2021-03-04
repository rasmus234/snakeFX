import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Board extends Application {

    public static boolean addTail = false;
    public static int score;
    public static int foodX;
    public static int foodY;
    public static SimpleIntegerProperty windowWidth = new SimpleIntegerProperty(900);
    public static SimpleIntegerProperty windowHeight = new SimpleIntegerProperty(900);
    public static SimpleIntegerProperty gameHeight = new SimpleIntegerProperty(800);
    public static SimpleIntegerProperty gameWidth = new SimpleIntegerProperty(800);
    public static SimpleIntegerProperty tileSize = new SimpleIntegerProperty(20);
    public static SimpleIntegerProperty gameSpeed = new SimpleIntegerProperty(20);
    public static Direction direction = Direction.DOWN;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, windowWidth.get(), windowHeight.get());
        root.setStyle("-fx-background-color:dimgray");

        Canvas canvas = new Canvas(gameWidth.get(), gameHeight.get());
        root.setCenter(canvas);
        canvas.heightProperty().bind(gameHeight);
        canvas.widthProperty().bind(gameWidth);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //set canvas background to black
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameWidth.get(), gameHeight.get());

        //create initial snake and draw it
        Snake snake = new Snake( new BodyPart(1, 4), new BodyPart(1, 3), new BodyPart(1, 2));
        for (BodyPart bodyPart : snake.getBodyParts()) {
            drawBody(gc,bodyPart);
        }
        //initial food
        newFood(gc);
        //get current direction
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            switch (key.getCode()) {
                case W:
                case UP:
                    direction = Direction.UP;
                    break;
                case LEFT:
                case A:
                    direction = Direction.LEFT;
                    break;
                case DOWN:
                case S:
                    direction = Direction.DOWN;
                    break;
                case RIGHT:
                case D:
                    direction = Direction.RIGHT;
                    break;
            }
        });

        new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {
                if (now - lastTick > 1000000000 / gameSpeed.get()) {

                    List<BodyPart> currentSnake = new ArrayList<>(snake.getBodyParts());
                    int currentScore = score;

                    BodyPart newHead = moveSnake(snake, gc);
                    if (checkCollisions(newHead, currentSnake, gc)) {
                        this.stop();
                    }
                    if (score > currentScore) {
                        snake.getBodyParts().add(currentSnake.get(currentSnake.size() - 1));
                        gameSpeed.set(gameSpeed.get() + 1);
                    }
                    System.out.println(score);
                    lastTick = now;
                }
            }
        }.start();

        primaryStage.setMinHeight(windowHeight.get());
        primaryStage.setMinWidth(windowWidth.get());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public boolean checkCollisions(BodyPart newHead, List<BodyPart> currentSnake, GraphicsContext gc) {
        int newX = newHead.getX() * tileSize.get();
        int newY = newHead.getY() * tileSize.get();
        int gameWidth = Board.gameWidth.get();
        int gameHeight = Board.gameHeight.get();
        if (newX > gameWidth || newX < 0 || newY > gameHeight || newY < 0) {
            return true;
        }
        for (BodyPart bodyPart : currentSnake) {
            if (bodyPart.equals(newHead)) {
                return true;
            }
        }
        if (newHead.getY() == foodY && newHead.getX() == foodX) {
            score++;
            addTail = true;
            newFood(gc);

        }
        return false;
    }

    public BodyPart moveSnake(Snake snake, GraphicsContext gc) {
        BodyPart currentHead = snake.getHead();
        int currentHeadX = currentHead.getX();
        int currentHeadY = currentHead.getY();

        BodyPart newHead = new BodyPart(currentHead.getX(), currentHead.getY());

        switch (direction) {

            case DOWN:
                newHead.setY(currentHeadY + 1);
                break;
            case UP:
                newHead.setY(currentHeadY - 1);
                break;
            case LEFT:
                newHead.setX(currentHeadX - 1);
                break;
            case RIGHT:
                newHead.setX(currentHeadX + 1);
        }
        snake.setHead(newHead);
        gc.setFill(Color.PINK);
        drawBody(gc, newHead);


        gc.setFill(Color.BLACK);
        BodyPart tail = snake.getTail();
        drawBody(gc, tail);
        snake.removeTail();
        return newHead;
    }

    public void drawBody(GraphicsContext gc, BodyPart... bodyParts) {
        for (BodyPart bodyPart : bodyParts) {
            gc.fillRect(bodyPart.getX() * tileSize.get(), bodyPart.getY() * tileSize.get(), tileSize.get(), tileSize.get());
        }
    }

    public void newFood(GraphicsContext gc) {

        Board.foodX = ThreadLocalRandom.current().nextInt(tileSize.get());
        Board.foodY = ThreadLocalRandom.current().nextInt(tileSize.get());

        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(foodX * tileSize.get(), foodY * tileSize.get(), tileSize.get(), tileSize.get());
    }
}
