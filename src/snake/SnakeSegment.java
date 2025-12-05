package snake;

public class SnakeSegment {

    private int x;
    private int y;

    public SnakeSegment(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // getters
    public int getX() { return x; }
    public int getY() { return y; }

    // update position
    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
}
