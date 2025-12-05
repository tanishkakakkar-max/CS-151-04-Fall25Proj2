package snake;

import java.util.ArrayList;

public class Snake {

    private ArrayList<SnakeSegment> segments = new ArrayList<>();
    private Direction currentDirection;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Snake(int startX, int startY) {
        // Start snake with one head segment
        segments.add(new SnakeSegment(startX, startY));
        currentDirection = Direction.RIGHT;
    }

    public ArrayList<SnakeSegment> getSegments() {
        return segments;
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void setDirection(Direction d) {
        this.currentDirection = d;
    }

    public boolean isOpposite(Direction d) {
        return (currentDirection == Direction.UP && d == Direction.DOWN)
            || (currentDirection == Direction.DOWN && d == Direction.UP)
            || (currentDirection == Direction.LEFT && d == Direction.RIGHT)
            || (currentDirection == Direction.RIGHT && d == Direction.LEFT);
    }


    public void move() {
        for (int i = segments.size() - 1; i > 0; i--) {
            SnakeSegment prev = segments.get(i - 1);
            segments.get(i).setPosition(prev.getX(), prev.getY());
        }

        SnakeSegment head = segments.get(0);
        int newX = head.getX();
        int newY = head.getY();

        switch (currentDirection) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        head.setPosition(newX, newY);
    }

    public void grow() {
        SnakeSegment tail = segments.get(segments.size() - 1);
        segments.add(new SnakeSegment(tail.getX(), tail.getY()));
    }
}
