package snake;

import java.util.Random;

public class Food {

    private int x;
    private int y;
    private Random random = new Random();

    public Food(int maxX, int maxY) {
        spawn(maxX, maxY);
    }

    public void spawn(int maxX, int maxY) {
        x = random.nextInt(maxX);
        y = random.nextInt(maxY);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}