package snake;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class GameBoard {

    private Canvas canvas;
    private int cellSize;
    private int rows;
    private int cols;

    public GameBoard(int rows, int cols, int cellSize) {
        this.rows = rows;
        this.cols = cols;
        this.cellSize = cellSize;
        this.canvas = new Canvas(cols * cellSize, rows * cellSize);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void render(Snake snake, Food food) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.RED);
        gc.fillOval(food.getX() * cellSize, food.getY() * cellSize, cellSize, cellSize);

        gc.setFill(Color.LIMEGREEN);
        for (SnakeSegment seg : snake.getSegments()) {
            gc.fillRect(seg.getX() * cellSize, seg.getY() * cellSize, cellSize, cellSize);
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
}