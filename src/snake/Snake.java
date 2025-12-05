package snake;

import java.util.ArrayList;

public class Snake {

    private ArrayList<SnakeSegment> segments = new ArrayList<>();
    private Direction currentDirection;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Snake(int startX, int startY) {
        
        segments.add(new SnakeSegment(startX, startY));
        currentDirection = Direction.RIGHT;
    }

    public ArrayList<SnakeSegment> getSegments() {
        return segments;
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void setDirection(Direction dir) {
        this.currentDirection = dir;
    }

    public void move() {
        
    }

    public void grow() {
        SnakeSegment tail = segments.get(segments.size() - 1);
        segments.add(new SnakeSegment(tail.getX(), tail.getY()));
    }
}
