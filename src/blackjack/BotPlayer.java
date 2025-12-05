package blackjack;

public class BotPlayer {
    public class BotPlayer extends Player {

        private final int hitThreshold; // e.g., 16

        public BotPlayer(String name, int startingBalance, int hitThreshold) {
            super(name, startingBalance);
            this.hitThreshold = hitThreshold;
        }

        @Override
        public boolean shouldHit() {
            return !busted && !standing && getHandValue() < hitThreshold;
        }
    }

}
